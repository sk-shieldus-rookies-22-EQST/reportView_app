package com.example.reportview_003.ui.purchase.action

import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.UserpointRequest
import com.example.reportview_003.model.purchase.UserpointResponse
import com.example.reportview_003.repository.PurchaseRepository

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
