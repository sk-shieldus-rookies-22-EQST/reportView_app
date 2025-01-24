package com.example.reportview_003.ui.auth.action

import android.content.Context
import com.example.reportview_003.api.AuthAPI
import com.example.reportview_003.model.auth.*
import com.example.reportview_003.repository.AuthRepository

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