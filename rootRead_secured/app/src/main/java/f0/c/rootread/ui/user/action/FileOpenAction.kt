package f0.c.rootread.ui.user.action

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import f0.c.rootread.App
import f0.c.rootread.api.KMSAPI
import f0.c.rootread.model.kms.GemerateRequest
import f0.c.rootread.network.NetworkClient
import f0.c.rootread.repository.KmsRepository
import f0.c.rootread.ui.user.activity.ViewerActivity
import f0.c.rootread.utils.DoRSAUtils
import f0.c.rootread.utils.SessionManager
import okhttp3.Request
import okio.IOException
import java.io.File
import java.io.FileOutputStream
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class FileOpenAction(private val context: Context) {

    private var downloadUrl: String? = null
    private var progressDialog: ProgressDialog? = null  // ✅ 프로그래스 다이얼로그 추가

    fun openFile(bookId: Long) {
        val userId = SessionManager.getUserID(context).toString()
        val gemerateRequest = GemerateRequest(user_id = userId, book_id = bookId.toString())
//        val gemerateRequest = GemerateRequest(user_id = "4", book_id = 248.toString())

        val app = context.applicationContext as App
        val kmsApi = app.KMSretrofit.create(KMSAPI::class.java)

        val kmsRepository = KmsRepository(kmsApi)
        showLoading("서버와 통신 중...")
        kmsRepository.generate(gemerateRequest) { data, error ->

            // 400 에러 또는 기타 오류 발생 시
            if (error != null) {
                showToast(error) // ✅ 서버에서 받은 에러 메시지 출력
                dismissLoading()
                return@generate
            }

            val presignedUrl = data?.signed_url
            // 정상적으로 signed_url이 반환되지 않은 경우
            if (presignedUrl.isNullOrEmpty()) {
                showToast("파일 다운로드 URL을 가져올 수 없습니다.") // ✅ 에러 메시지 표시
                dismissLoading()
                return@generate
            }

            showLoading("책 여는 중...")

            downloadFile(presignedUrl) { downloadedFile ->
                if (downloadedFile != null) {
                    val doRSAUtils = DoRSAUtils(kmsRepository,presignedUrl)
                    doRSAUtils.getKeysAsync { key, iv -> // 🔹 AES Key와 IV를 비동기적으로 가져온 후 실행
                        if (key == null || iv == null) {
                            showToast("암호를 가져오는 데 실패했습니다.")
                            return@getKeysAsync
                        }

                        try {
                            val encryptedData = downloadedFile.readBytes()

                            Log.d("KEYCHECK", "key: ${key} / iv: ${iv}")

                            val aesKey = key.toByteArray(Charsets.UTF_8).copyOf(16)
                            val aesIv = iv.toByteArray(Charsets.UTF_8).copyOf(16)

                            val decryptedData = decryptAES(encryptedData, aesKey, aesIv)
                            if (decryptedData != null) {
                                saveDecryptedFileAsPdf(decryptedData)
                            } else {
                                showToast("복호화 실패")
                            }
                        } catch (e: Exception) {
                            showToast("에러 발생: ${e.message}")
                        } finally {
                            doRSAUtils.clearKeys()
                            dismissLoading()
                        }
                    }
                } else {
                    showToast("파일 다운로드 실패")
                    dismissLoading()
                }
                dismissLoading()
            }
        }
    }

    private fun downloadFile(url: String, callback: (File?) -> Unit) {
        Log.d("CHECKFILE", "downloadFile 실행됨")
        val request = Request.Builder()
            .url(url)
            // .addHeader("Connection", "close")  // 연결 종료 강제 필요 시 사용
            .build()

        Thread {
            Log.d("CHECKFILE", "Thread 실행됨")
            try {
                val response = NetworkClient.client.newCall(request).execute()
                Log.d("CHECKFILE", "response 요청")
                response.use { res ->
                    if (!res.isSuccessful) {
                        Log.e("FileDownload", "서버 응답 오류: ${res.code}")
                        postToMainThread { callback(null) }
                        return@use
                    }

                    val body = res.body
                    if (body == null) {
                        Log.e("FileDownload", "응답 바디가 null입니다.")
                        postToMainThread { callback(null) }
                        return@use
                    }

                    val downloadedFile = File(context.cacheDir, "downloaded_file")
                    try {
                        body.byteStream().use { inputStream ->
                            FileOutputStream(downloadedFile).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                        Log.d("FileDownload", "파일 다운로드 성공: ${downloadedFile.absolutePath}")
                        postToMainThread { callback(downloadedFile) }
                    } catch (ioe: IOException) {
                        Log.e("FileDownload", "파일 저장 중 오류 발생: ${ioe.message}", ioe)
                        postToMainThread { callback(null) }
                    }
                }
            } catch (e: Exception) {
                Log.e("FileDownload", "파일 다운로드 중 오류 발생: ${e.message}")
                postToMainThread { callback(null) }
            }
        }.start()
    }

    // UI 업데이트가 필요한 callback은 메인 스레드로 전환해서 호출
    private fun postToMainThread(action: () -> Unit) {
        Handler(Looper.getMainLooper()).post { action() }
    }


    private fun decryptAES(encryptedData: ByteArray, key: ByteArray, iv: ByteArray): ByteArray? {
        return try {
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
            cipher.doFinal(encryptedData)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveDecryptedFileAsPdf(decryptedData: ByteArray) {
        try {
            val fileName = "decrypted_file.png"
            val cacheFolder = context.cacheDir
            val file = File(cacheFolder, fileName)

            FileOutputStream(file).use { fos ->
                fos.write(decryptedData)
            }
            openPdfViewer(file.absolutePath) // ✅ PDF 실행
        } catch (e: Exception) {
            showToast("파일 저장 오류: ${e.message}")
        }
    }

    private fun openPdfViewer(file: String) {
        Log.d("FileOpenAction", "Opening PdfViewer with path: $file")
        val intent = Intent(context, ViewerActivity::class.java).apply {
            putExtra("imgPath", file)  // ✅ PDF 파일 경로 전달
        }
        context.startActivity(intent)
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // ✅ 프로그래스 다이얼로그 표시
    private fun showLoading(message: String) {
        Handler(Looper.getMainLooper()).post {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context).apply {
                    setCancelable(false)
                    setMessage(message)
                    setProgressStyle(ProgressDialog.STYLE_SPINNER)
                }
            }
            progressDialog?.show()
        }
    }

    // ✅ 프로그래스 다이얼로그 닫기
    private fun dismissLoading() {
        Handler(Looper.getMainLooper()).post {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }
}
