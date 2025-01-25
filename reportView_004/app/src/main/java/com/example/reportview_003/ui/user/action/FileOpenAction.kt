package com.example.reportview_003.ui.user.action

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.reportview_003.App
import com.example.reportview_003.api.KMSAPI
import com.example.reportview_003.model.kms.GemerateRequest
import com.example.reportview_003.ui.user.activity.PdfActivity
import com.example.reportview_003.utils.SessionManager
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class FileOpenAction(private val context: Context) {

    private var download_url: String? = null
    private val client = OkHttpClient()

    fun openFile(book_id: String) {
        val user_id = SessionManager.getUserID(context).toString()
        val gemerateRequest = GemerateRequest(user_id = user_id, book_id = book_id)

        val app = context.applicationContext as App
        val kmsapi = app.KMSretrofit.create(KMSAPI::class.java)

        val getGenerate = GetGenerate(context, kmsapi)
        getGenerate.generate(gemerateRequest) { data ->
            if (data != null && !data.presigned_url.isNullOrEmpty()) {
                download_url = data.presigned_url
                downloadFile(download_url!!) { downloadedFile ->
                    if (downloadedFile != null) {
                        try {
                            val encryptedData = downloadedFile.readBytes()
                            val aesKey = "ROOKIES".toByteArray(Charsets.US_ASCII).copyOf(16)
                            val aesIv = "EQST".toByteArray(Charsets.US_ASCII).copyOf(16)

                            val decryptedData = decryptAES(encryptedData, aesKey, aesIv)

                            if (decryptedData != null && isPdfFile(decryptedData)) {
                                saveDecryptedFileAsPdf(decryptedData)
                            } else {
                                showToast("복호화 실패 또는 PDF 형식이 아님.")
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
    }

    private fun downloadFile(url: String, callback: (File?) -> Unit) {
        val request = Request.Builder().url(url).build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val downloadedFile = File(context.cacheDir, "downloaded_file")
                    val fos = FileOutputStream(downloadedFile)
                    fos.write(response.body?.bytes())
                    fos.close()
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
            val secretKeySpec = SecretKeySpec(key, "AES")
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)
            cipher.doFinal(encryptedData)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun isPdfFile(data: ByteArray): Boolean {
        return try {
            val header = String(data.copyOfRange(0, 4), Charsets.US_ASCII)
            header.startsWith("%PDF")
        } catch (e: Exception) {
            false
        }
    }

    private fun saveDecryptedFileAsPdf(decryptedData: ByteArray) {
        try {
            val fileName = "decrypted_file.pdf"
            val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadFolder, fileName)

            val fos = FileOutputStream(file)
            fos.write(decryptedData)
            fos.close()

            showToast("복호화된 PDF 파일이 저장되었습니다: ${file.absolutePath}")

            openPdfViewer(file.absolutePath)
        } catch (e: Exception) {
            showToast("파일 저장 중 오류 발생: ${e.message}")
        }
    }

    private fun openPdfViewer(pdfPath: String) {
        Log.d("FileOpenAction", "Opening PdfViewer with path: $pdfPath")
        val intent = Intent(context, PdfActivity::class.java).apply {
            putExtra("PDF_PATH", pdfPath)
        }
        context.startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
