package com.example.reportview_003.ui.auth.action

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import com.example.reportview_003.R
import com.example.reportview_003.api.AuthAPI
import com.example.reportview_003.model.auth.LoginRequest
import com.example.reportview_003.utils.SessionManager
import com.example.reportview_003.ActiveMain
import com.example.reportview_003.model.auth.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val loginData = LoginRequest (
            user_id = id,
            user_pw = pw
        )

        authAPI.login(loginData).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    SessionManager.saveLoginSession(context, id)
                    callback(true)
                    navController.navigate(R.id.action_loginFragment_to_listFragment)
                    (context as ActiveMain).updateNavigationMenu()
                } else {
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "로그인 실패: ${t.message}", Toast.LENGTH_SHORT).show()
                callback(false)
            }
        })

    }
}
