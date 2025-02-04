package com.example.bookies_001.ui.user.action

import android.content.Context
import com.example.bookies_001.api.UserAPI
import com.example.bookies_001.model.user.UserpurchaseRequest
import com.example.bookies_001.model.user.UserpurchaseResponse
import com.example.bookies_001.repository.UserRepository

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