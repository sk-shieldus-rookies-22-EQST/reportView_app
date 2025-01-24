package com.example.reportview_003.ui.user.action

import android.content.Context
import com.example.reportview_003.api.UserAPI
import com.example.reportview_003.model.user.UserbooklistRequest
import com.example.reportview_003.model.user.UserbooklistResponse
import com.example.reportview_003.repository.UserRepository

class GetUserBookList(
    private val context: Context,
    private val userAPI: UserAPI
) {
    fun loadUserBooklist(
        userbooklistRequest: UserbooklistRequest,
        callback: (UserbooklistResponse?) -> Unit
    ) {
        val userRepository = UserRepository(userAPI)

        userRepository.userBooklist(userbooklistRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}