package com.example.rootread.ui.purchase.action

import com.example.rootread.api.PurchaseAPI
import com.example.rootread.model.purchase.UserpointRequest
import com.example.rootread.model.purchase.UserpointResponse
import com.example.rootread.repository.PurchaseRepository

class GetUserPoint(
    private val purchaseAPI: PurchaseAPI
) {
    fun getUserPoint(
        userpointRequest: UserpointRequest,
        callback: (UserpointResponse?, Throwable?) -> Unit
    ) {
        val perchaseRepository = PurchaseRepository(purchaseAPI)
        perchaseRepository.userPoint(userpointRequest) { response, error ->
            if (response != null) {
                callback(response, null)
            } else {
                callback(null, Throwable("failed"))
            }
        }
    }
}
