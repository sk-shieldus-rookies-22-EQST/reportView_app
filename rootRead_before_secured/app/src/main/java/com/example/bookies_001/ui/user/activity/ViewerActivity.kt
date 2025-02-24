package com.example.bookies_001.ui.user.activity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.bookies_001.R
import java.io.File

class ViewerActivity : AppCompatActivity() {

    private lateinit var imgViewer: ImageView
    private var imgPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FLAG_SECURE 추가 (스크린샷 방지)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContentView(R.layout.viewer)

        imgPath = intent.getStringExtra("imgPath")
        println(imgPath)
        val imgFile = File(imgPath)

        imgViewer = findViewById(R.id.img_viewer)

        Glide.with(this)
            .asBitmap()  // Bitmap으로 로드하여 크기를 측정합니다.
            .load(imgFile)
            .skipMemoryCache(true)  // 메모리 캐시 사용 안 함
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 사용 안 함
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // 이미지뷰에 Bitmap 설정
                    imgViewer.setImageBitmap(resource)

                    // 화면 폭 가져오기
                    val displayMetrics: DisplayMetrics = resources.displayMetrics
                    val screenWidth = displayMetrics.widthPixels

                    // 이미지의 가로세로 비율 계산
                    val ratio: Float = resource.height.toFloat() / resource.width.toFloat()

                    // 화면 폭에 맞게 높이 계산
                    val calculatedHeight = (screenWidth * ratio).toInt()

                    // 레이아웃 파라미터 수정
                    val params = imgViewer.layoutParams
                    params.height = calculatedHeight
                    imgViewer.layoutParams = params
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // 필요시 placeholder 처리
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        imgPath?.let { path ->
            // 원본 이미지 파일 삭제
            val originalFile = File(path)
            if (originalFile.exists()) {
                originalFile.delete()
            }

            // 동일 경로의 "downloaded_file" 삭제
            originalFile.parent?.let { parentPath ->
                val downloadedFile = File(parentPath, "downloaded_file")
                if (downloadedFile.exists()) {
                    downloadedFile.delete()
                }
            }
        }
    }
}
