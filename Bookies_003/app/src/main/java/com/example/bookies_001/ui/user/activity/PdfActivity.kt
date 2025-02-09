package com.example.bookies_001.ui.user.activity

import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.bookies_001.R
import java.io.File

class PdfActivity : AppCompatActivity() {

    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var fileDescriptor: ParcelFileDescriptor
    private lateinit var viewPager: ViewPager2
    private var pageCount: Int = 0
    private var pdfFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FLAG_SECURE 추가 (스크린샷 방지)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContentView(R.layout.activity_pdf)

        // ✅ FileOpenAction에서 전달된 PDF 경로 받기
        pdfFilePath = intent.getStringExtra("PDF_PATH") ?: ""
        if (pdfFilePath.isNullOrEmpty()) {
            Toast.makeText(this, "PDF 경로가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewPager = findViewById(R.id.pdfViewPager)

        try {
            openPdfRenderer(pdfFilePath!!)
            pageCount = pdfRenderer.pageCount

            val adapter = PdfPagerAdapter(pdfRenderer, pageCount)
            viewPager.adapter = adapter
        } catch (e: Exception) {
            Log.e("PdfActivity", "PDF 열기 실패: ${e.message}")
            Toast.makeText(this, "PDF를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun openPdfRenderer(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            Toast.makeText(this, "PDF 파일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        try {
            fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfRenderer = PdfRenderer(fileDescriptor)
            Log.d("PdfActivity", "PDF Renderer 열기 성공.")
        } catch (e: Exception) {
            Log.e("PdfActivity", "PDF Renderer 열기 실패: ${e.message}")
            Toast.makeText(this, "PDF를 열 수 없습니다: ${e.message}", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pdfRenderer.close()
        fileDescriptor.close()

        // PDF 파일 삭제
        pdfFilePath?.let {
            val file = File(it)
            if (file.exists()) {
                val deleted = file.delete()
                if (deleted) {
                    Log.d("PdfActivity", "임시 PDF 파일 삭제 성공: $pdfFilePath")
                } else {
                    Log.e("PdfActivity", "임시 PDF 파일 삭제 실패: $pdfFilePath")
                }
            }
        }
    }
}
