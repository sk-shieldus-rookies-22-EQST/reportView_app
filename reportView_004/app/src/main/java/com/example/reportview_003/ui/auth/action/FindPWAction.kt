package com.example.reportview_003.ui.auth.action

import android.content.Context
import com.example.reportview_003.api.AuthAPI
import com.example.reportview_003.model.auth.FindPWRequest
import com.example.reportview_003.repository.AuthRepository

class FindPWAction(
    private val context: Context,
    private val authApi: AuthAPI
) {
    fun doFindPW(findPWRequest: FindPWRequest, callback: (String?) -> Unit) {
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