package com.example.bookies_001.ui.auth.action

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import com.example.bookies_001.R
import com.example.bookies_001.api.AuthAPI
import com.example.bookies_001.model.auth.LoginRequest
import com.example.bookies_001.utils.SessionManager
import com.example.bookies_001.ActiveMain
import com.example.bookies_001.repository.AuthRepository
import com.example.bookies_001.utils.AESUtil

class LoginAction {

    fun doLogin(
        context: Context,
        idField: EditText,
        pwField: EditText,
        authAPI: AuthAPI,
        navController: NavController,
        callback: (Boolean) -> Unit
    ) {
        val id = idField.text.toString()
        val pw = pwField.text.toString()

        if (id.isBlank() || pw.isBlank()) {
            Toast.makeText(context, "ID와 PW를 입력하세요.", Toast.LENGTH_SHORT).show()
            callback(false)
            return
        }

        val loginData = id+"&&&&"+pw
//
        val aesEncrypt = LoginRequest(
            AESUtil.encrypt(loginData)
        )

        AuthRepository(authAPI).login(aesEncrypt) { response, error ->
            if (response != null) {
                if (response.status) {
                    SessionManager.saveLoginSession(context, id)
                    callback(true)
                    navController.navigate(R.id.action_loginFragment_to_listFragment)
                    (context as ActiveMain).updateNavigationMenu()
                } else {
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
            } else {
                Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                error?.printStackTrace()
            }
        }


    }
}
