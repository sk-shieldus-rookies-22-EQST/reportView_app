package com.example.reportview_003.ui.auth.action

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.reportview_003.ActiveMain
import com.example.reportview_003.R
import com.example.reportview_003.api.AuthAPI
import com.example.reportview_003.repository.AuthRepository
import com.example.reportview_003.utils.SessionManager

class LoginAction() {

    fun doLogin(context: Context, idField: EditText, pwField: EditText, authAPI: AuthAPI) {
        val id = idField.text.toString()
        val pw = pwField.text.toString()

        val authRepository = AuthRepository(authAPI)

        authRepository.login(id, pw) { response, error ->
            if (response != null) {
                Toast.makeText(context, "$response welcome", Toast.LENGTH_SHORT).show()
                // 로그인 세션 저장
                val userToken = "your_auth_token"
                SessionManager.saveLoginSession(context, userToken)

                if (context is ActiveMain) {
                    context.onLoginSuccess()
                }

                if (context is FragmentActivity) {
                    val viewPager = (context as AppCompatActivity).findViewById<ViewPager2>(R.id.view_pager)
                    // ViewPager2의 현재 페이지를 ListFragment로 변경
                    viewPager.currentItem = 1
                } else {
                    Toast.makeText(context, "Unable to navigate to the fragment.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "error: $error", Toast.LENGTH_SHORT).show()
            }

        }

    }
}