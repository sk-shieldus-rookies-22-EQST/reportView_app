package com.example.bookies_001.ui.purchase.action

import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.model.purchase.UserpointRequest
import com.example.bookies_001.model.purchase.UserpointResponse
import com.example.bookies_001.repository.PurchaseRepository

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
