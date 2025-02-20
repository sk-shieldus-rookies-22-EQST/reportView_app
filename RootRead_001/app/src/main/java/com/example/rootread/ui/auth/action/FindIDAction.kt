package com.example.rootread.ui.auth.action

import android.content.Context
import com.example.rootread.api.AuthAPI
import com.example.rootread.model.auth.FindIDRequest
import com.example.rootread.repository.AuthRepository

class FindIDAction(
    private val context: Context,
    private val authAPI: AuthAPI
) {

    fun doFindid(findIDRequest: FindIDRequest, callback: (String?) -> Unit) {
        val authRepository = AuthRepository(authAPI)

        authRepository.findID(findIDRequest) { response, error ->
            if (response != null) {
                callback(response.user_id)
            } else {
                error?.printStackTrace()
            }

        }
    }
}