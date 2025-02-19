package com.example.bookies_001.ui.auth.action

import android.content.Context
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import com.example.bookies_001.R
import com.example.bookies_001.api.AuthAPI
import com.example.bookies_001.model.auth.LoginRequest
import com.example.bookies_001.utils.SessionManager
import com.example.bookies_001.ActiveMain
import com.example.bookies_001.App
import com.example.bookies_001.api.KMSAPI
import com.example.bookies_001.repository.AuthRepository
import com.example.bookies_001.repository.KmsRepository
import com.example.bookies_001.utils.AESUtil
import com.example.bookies_001.utils.DoRSAUtils

class LoginAction {

    fun doLogin(
        context: Context,
        idField: EditText,
        pwField: EditText,
        remmemberID: CheckBox,
        autoLogin: CheckBox,
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

        val app = context.applicationContext as App
        val kmsApi = app.KMSretrofit.create(KMSAPI::class.java)

        val kmsRepository = KmsRepository(kmsApi)

        val loginData = id+"&&&&"+pw

        AESUtil.encrypt(loginData,kmsRepository) { encryptedData ->
            if (encryptedData != null) {
                val aesEncrypt = LoginRequest(e2e_data = encryptedData) // 🔹 암호화된 데이터 설정

                AuthRepository(authAPI).login(aesEncrypt) { response, error ->
                    if (response != null) {
                        if (response.status) {
                            SessionManager.saveAutoLogin(context,autoLogin.isChecked)
                            SessionManager.saveRememberID(context,remmemberID.isChecked)
                            SessionManager.saveLoginSession(context, id)
                            AuthRepository(authAPI).userLevel(id) { res ->
                                if (res != null) {
                                    res.user_level?.let { SessionManager.saveUserLevel(context, it) }
                                }
                            }
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
            } else {
                Log.e("Login", "AES 암호화 실패")
            }
        }

    }
}
