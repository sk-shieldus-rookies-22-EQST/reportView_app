package com.example.bookies_001.ui.auth.action

import android.content.Context
import com.example.bookies_001.api.AuthAPI
import com.example.bookies_001.model.auth.FindPWRequest
import com.example.bookies_001.repository.AuthRepository

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