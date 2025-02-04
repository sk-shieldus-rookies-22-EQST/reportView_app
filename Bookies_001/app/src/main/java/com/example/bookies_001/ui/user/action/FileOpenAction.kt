package com.example.bookies_001.ui.user.action

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
import com.example.bookies_001.ui.user.activity.PdfActivity
import com.example.bookies_001.utils.AESUtil
import com.example.bookies_001.utils.SessionManager
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class FileOpenAction(private val context: Context) {

    private var downloadUrl: String? = null
    private val client = OkHttpClient()

    fun openFile(bookId: Long) {
        val userId = SessionManager.getUserID(context).toString()
//        val gemerateRequest = GemerateRequest(user_id = userId, book_id = bookId.toString())
        val gemerateRequest = GemerateRequest(user_id = "123", book_id = 1000020.toString())

        val app = context.applicationContext as App
        val kmsApi = app.KMSretrofit.create(KMSAPI::class.java)

        val getGenerate = GetGenerate(kmsApi)
        getGenerate.generate(gemerateRequest) { data ->
            if (data?.presigned_url.isNullOrEmpty()) {
                showToast("파일 다운로드 URL을 가져올 수 없습니다.")
                return@generate
            }

            if (data?.error.isNullOrEmpty()) {
                showToast("파일 다운로드 URL을 가져올 수 없습니다.")
            }

            downloadUrl = data?.presigned_url
            downloadFile(downloadUrl!!) { downloadedFile ->
                if (downloadedFile != null) {
                    try {
                        val encryptedData = downloadedFile.readBytes()
                        val aesKey = AESUtil.key.toByteArray(Charsets.US_ASCII).copyOf(16)
                        val aesIv = AESUtil.iv.toByteArray(Charsets.US_ASCII).copyOf(16)

                        val decryptedData = decryptAES(encryptedData, aesKey, aesIv)
                        Log.d("decryptedData", "Decrypted Data Size: ${decryptedData?.size}")

                        if (decryptedData != null) {
                            saveDecryptedFileAsPdf(decryptedData)
                        } else {
                            showToast("복호화 실패")
                        }
                    } catch (e: Exception) {
                        showToast("에러 발생: ${e.message}")
                    }
                } else {
                    showToast("파일 다운로드 실패")
                }
            }
        }
    }

    private fun downloadFile(url: String, callback: (File?) -> Unit) {
        val request = Request.Builder().url(url).build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val downloadedFile = File(context.cacheDir, "downloaded_file")
                    FileOutputStream(downloadedFile).use { fos ->
                        fos.write(response.body?.bytes())
                    }

                    if (downloadedFile.exists()) {
                        Log.d("FileDownload", "Downloaded file path: ${downloadedFile.absolutePath}")
                    } else {
                        Log.e("FileDownload", "파일이 생성되지 않았습니다.")
                    }

                    callback(downloadedFile)
                } else {
                    callback(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
            val fileName = "decrypted_file.pdf"
            val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadFolder, fileName)

            FileOutputStream(file).use { fos ->
                fos.write(decryptedData)
            }

            showToast("복호화된 PDF 저장 완료: ${file.absolutePath}")
            openPdfViewer(file.absolutePath) // ✅ PDF 실행
        } catch (e: Exception) {
            showToast("파일 저장 오류: ${e.message}")
        }
    }

    private fun openPdfViewer(pdfPath: String) {
        Log.d("FileOpenAction", "Opening PdfViewer with path: $pdfPath")
        val intent = Intent(context, PdfActivity::class.java).apply {
            putExtra("PDF_PATH", pdfPath)  // ✅ PDF 파일 경로 전달
        }
        context.startActivity(intent)
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
