package com.example.reportview_003.ui.user.action

import android.content.Context
import com.example.reportview_003.api.UserAPI
import com.example.reportview_003.model.user.UserpurchaseRequest
import com.example.reportview_003.model.user.UserpurchaseResponse
import com.example.reportview_003.repository.UserRepository

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