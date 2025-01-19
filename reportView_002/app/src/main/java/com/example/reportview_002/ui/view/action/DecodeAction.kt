package com.example.reportview_002.ui.view.action

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.reportview_002.ui.view.ReportView

/*
    로컬에 저장된 파일을 받아서 사용자로부터 입력받은 깂과 조합하여 pdf로 변환하는 로직
    정상적으로 처리된 경우 pdf를 반환
*/
class DecodeAction() {

    fun doDecode(
        context: Context,
        userInput: String,
        reportName: String
    ) {
        if (true){
            val intent = Intent(context, ReportView::class.java)
            context.startActivity(intent)
        }
        else {
            Toast.makeText(context, "암호가 틀렸습니다.", Toast.LENGTH_SHORT).show()

        }
    }

    /*
    * 복호화 로직에 해당 하는 메서드
    * 외부에서 사용 할 수 없도록 privete로 구성
    * */
    private fun decodeProccess() {

    }
}