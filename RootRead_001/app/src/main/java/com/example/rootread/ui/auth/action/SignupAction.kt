package com.example.rootread.ui.auth.action

import android.content.Context
import com.example.rootread.api.Api
import com.example.rootread.model.api.SignupRequest
import com.example.rootread.repository.SignupRepository

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