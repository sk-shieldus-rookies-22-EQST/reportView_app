package com.example.reportview_003.ui.view.action

import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.reportview_003.R

class FileOpenAction() {

    fun openFile(
        context: Context,
        title: String
    ) {
        val decodeAction = DecodeAction()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_input, null)
        val inputEditText: EditText = dialogView.findViewById(R.id.dialog_input)

        val dialog = AlertDialog.Builder(context)
            .setTitle("$title 열기")
            .setMessage("암호를 입력해 주세요")
            .setView(dialogView)
            .setPositiveButton("확인"){ dialog, _ ->
//              사용자 입력 값
                val userInput = inputEditText.text.toString()

//              디코드 동작
                decodeAction.doDecode(context,userInput, title)
            }
            .setNeutralButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}