package com.example.bookies_001.ui.user.action

import android.content.Context
import com.example.bookies_001.api.UserAPI
import com.example.bookies_001.model.user.UserbooklistRequest
import com.example.bookies_001.model.user.UserbooklistResponse
import com.example.bookies_001.repository.UserRepository

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