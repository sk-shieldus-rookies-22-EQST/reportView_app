package com.example.rootread.ui.user.action

import android.content.Context
import com.example.rootread.api.UserAPI
import com.example.rootread.model.user.UserpurchaseRequest
import com.example.rootread.model.user.UserpurchaseResponse
import com.example.rootread.repository.UserRepository

class GetUserPurchase(
    private val context: Context,
    private val userAPI: UserAPI
) {
    fun loadUserPurchase(
        userpurchaseRequest: UserpurchaseRequest,
        callback: (UserpurchaseResponse?) -> Unit
    ) {
        val userPurchase = UserRepository(userAPI)

        userPurchase.userPurchase(userpurchaseRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}