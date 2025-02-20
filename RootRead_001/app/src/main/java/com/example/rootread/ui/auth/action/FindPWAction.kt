package com.example.rootread.ui.auth.action

import android.content.Context
import com.example.rootread.api.AuthAPI
import com.example.rootread.model.auth.FindPWRequest
import com.example.rootread.repository.AuthRepository

class FindPWAction(
    private val context: Context,
    private val authApi: AuthAPI
) {
    fun doFindPW(findPWRequest: FindPWRequest, callback: (Boolean?) -> Unit) {
        val findPWRepository = AuthRepository(authApi)

        findPWRepository.findPW(findPWRequest) { response, error ->
            if (response != null) {
                callback(response.status)
            } else {
                error?.printStackTrace()
            }

        }
    }
}