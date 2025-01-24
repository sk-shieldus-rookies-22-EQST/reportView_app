package com.example.reportview_003.ui.auth.action

import android.content.Context
import com.example.reportview_003.api.Api
import com.example.reportview_003.model.api.SignupRequest
import com.example.reportview_003.repository.SignupRepository

class SignupAction(
    private val context: Context,
    private val api: Api
) {

    fun doSignup(signupRequest: SignupRequest, callback: (Boolean?) -> Unit) {
        val signupRepository = SignupRepository(api)

        signupRepository.signup(signupRequest) { response, error ->
            if (response != null) {
                callback(response.status)
            } else {
                error?.printStackTrace()
            }

        }
    }
}