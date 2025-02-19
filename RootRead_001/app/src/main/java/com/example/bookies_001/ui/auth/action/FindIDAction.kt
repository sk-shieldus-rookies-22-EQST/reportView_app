package com.example.bookies_001.ui.auth.action

import android.content.Context
import com.example.bookies_001.api.AuthAPI
import com.example.bookies_001.model.auth.FindIDRequest
import com.example.bookies_001.repository.AuthRepository

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