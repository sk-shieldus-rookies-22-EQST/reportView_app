package com.example.reportview_003.ui.auth.action

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.navigation.NavController
import com.example.reportview_003.R
import com.example.reportview_003.api.AuthAPI
import com.example.reportview_003.model.auth.LoginRequest
import com.example.reportview_003.repository.AuthRepository
import com.example.reportview_003.utils.SessionManager

class LoginAction() {

    fun doLogin(
        context: Context,
        idField: EditText,
        pwField: EditText,
        authAPI: AuthAPI,
        navContoller: NavController
    ) {
        val id = idField.text.toString()
        val pw = pwField.text.toString()

        val logindata = LoginRequest(
            userid = id,
            passwd = pw
        )

        val authRepository = AuthRepository(authAPI)

        authRepository.login(logindata) { response, error ->
            if (response != null) {
                Toast.makeText(context, "$response welcome", Toast.LENGTH_SHORT).show()

                // 로그인 성공 시 세션 저장
                val userToken = "your_auth_token"
                SessionManager.saveLoginSession(context, userToken)

                // 로그인 성공 시 ListFragment로 이동
                navContoller.navigate(R.id.action_login_to_list)

            } else {
                Toast.makeText(context, "error: $error", Toast.LENGTH_SHORT).show()
            }

        }

    }
}