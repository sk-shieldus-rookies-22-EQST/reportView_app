package com.example.bookies_001.ui.user.action

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.bookies_001.App
import com.example.bookies_001.api.KMSAPI
import com.example.bookies_001.model.kms.GemerateRequest
import com.example.bookies_001.network.NetworkClient
import com.example.bookies_001.repository.KmsRepository
import com.example.bookies_001.ui.user.activity.PdfActivity
import com.example.bookies_001.ui.user.activity.ViewerActivity
import com.example.bookies_001.utils.AESUtil
import com.example.bookies_001.utils.SessionManager
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
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

            // 정상적으로 presigned_url이 반환되지 않은 경우
            if (data?.presigned_url.isNullOrEmpty()) {
                showToast("파일 다운로드 URL을 가져올 수 없습니다.") // ✅ 에러 메시지 표시
                dismissLoading()
                return@generate
            }

            downloadUrl = data?.presigned_url
            showLoading("책 여는 중...")

            downloadFile(downloadUrl!!) { downloadedFile ->
                if (downloadedFile != null) {
                    val doRSA = DoRSA(kmsRepository)
                    doRSA.getKeysAsync { key, iv -> // 🔹 AES Key와 IV를 비동기적으로 가져온 후 실행
                        if (key == null || iv == null) {
                            showToast("복호화 암호를 가져오는 데 실패했습니다.")
                            return@getKeysAsync
                        }

                        try {
                            val encryptedData = downloadedFile.readBytes()

                            val aesKey = key.copyOf(16)
                            val aesIv = iv.copyOf(16)

                            val decryptedData = decryptAES(encryptedData, aesKey, aesIv)
                            if (decryptedData != null) {
                                saveDecryptedFileAsPdf(decryptedData)
                            } else {
                                showToast("복호화 실패")
                            }
                        } catch (e: Exception) {
                            showToast("에러 발생: ${e.message}")
                        } finally {
                            doRSA.close()
                            dismissLoading()
                        }
                    }
                } else {
                    showToast("파일 다운로드 실패")
                    dismissLoading()
                }
            }
        }
    }

    private fun downloadFile(url: String, callback: (File?) -> Unit) {
        val request = Request.Builder()
            .url(url)
//            .addHeader("Connection", "close")  // ✅ 연결 종료 강제
            .build()

        Thread {
            try {
                val response = NetworkClient.client.newCall(request).execute()

                response.use { res ->  // ✅ response 자동 닫기
                    if (res.isSuccessful) {
                        val downloadedFile = File(context.cacheDir, "downloaded_file")

                        res.body?.byteStream()?.use { inputStream ->
                            FileOutputStream(downloadedFile).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }

                        callback(downloadedFile)
                    } else {
                        Log.e("FileDownload", "서버 응답 오류: ${res.code}")
                        callback(null)
                    }
                }
            } catch (e: Exception) {
                Log.e("FileDownload", "파일 다운로드 중 오류 발생: ${e.message}")
                callback(null)
            }
        }.start()
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
