package com.example.reportview_002.ui.login.action

import android.content.Context
import android.content.Intent
import android.widget.EditText
import android.widget.Toast
import com.example.reportview_002.api.AuthAPI
import com.example.reportview_002.repository.AuthRepository
import com.example.reportview_002.ui.view.ListMain

class LoginAction() {

    fun doLogin(context: Context, idField: EditText, pwField: EditText, authAPI: AuthAPI) {
        val id = idField.text.toString()
        val pw = pwField.text.toString()

        val authRepository = AuthRepository(authAPI)

        authRepository.login(id, pw) { response, error ->
            if (response != null) {
                Toast.makeText(context, "$response welcome", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, ListMain::class.java)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "error: $error", Toast.LENGTH_SHORT).show()
            }

        }

    }
}