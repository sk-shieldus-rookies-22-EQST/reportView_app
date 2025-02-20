package com.example.rootread.ui.user.action

import android.content.Context
import com.example.rootread.api.UserAPI
import com.example.rootread.model.user.UserbooklistRequest
import com.example.rootread.model.user.UserbooklistResponse
import com.example.rootread.repository.UserRepository

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