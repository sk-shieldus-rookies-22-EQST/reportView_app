package com.example.bookies_001.ui.auth.action

import android.content.Context
import com.example.bookies_001.api.Api
import com.example.bookies_001.model.api.SignupRequest
import com.example.bookies_001.repository.SignupRepository

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