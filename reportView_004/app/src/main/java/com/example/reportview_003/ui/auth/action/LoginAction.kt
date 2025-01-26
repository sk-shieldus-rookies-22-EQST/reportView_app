package com.example.reportview_003.ui.auth.action

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import com.example.reportview_003.R
import com.example.reportview_003.api.AuthAPI
import com.example.reportview_003.model.auth.LoginRequest
import com.example.reportview_003.repository.AuthRepository
import com.example.reportview_003.utils.SessionManager
import com.example.reportview_003.ActiveMain

class LoginAction {

    fun doLogin(
        context: Context,
        idField: EditText,
        pwField: EditText,
        authAPI: AuthAPI,
        navController: NavController
    ) {
        val id = idField.text.toString()
        val pw = pwField.text.toString()

        val loginData = LoginRequest(
            userid = id,
            passwd = pw
        )

        val authRepository = AuthRepository(authAPI)

        authRepository.login(loginData) { response, error ->
            if (response != null && response.status) {
                Toast.makeText(context, "Welcome ${id}", Toast.LENGTH_SHORT).show()

                // Save login session
                SessionManager.saveLoginSession(context, true.toString())
                SessionManager.saveUserID(context, id) // Save user ID

                // Update the navigation menu
                if (context is ActiveMain) {
                    context.updateNavigationMenu()
                }

                // Navigate to ListFragment or other desired screen
                navController.navigate(R.id.action_login_to_list)
            } else {
                Toast.makeText(context, "Login failed: ${error?.message ?: "Unknown error"}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
