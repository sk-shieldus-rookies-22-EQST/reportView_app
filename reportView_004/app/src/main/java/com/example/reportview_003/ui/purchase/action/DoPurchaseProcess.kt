package com.example.reportview_003.ui.purchase.action

import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.*
import com.example.reportview_003.repository.PurchaseRepository

class DoPurchaseProcess(
    private val purchaseAPI: PurchaseAPI
) {
    fun doPurchaseProcess(
        perchaseProccessRequest: PerchaseProccessRequest,
        callback: (PerchaseProccessResponse?) -> Unit) {
        val purchaseRepository = PurchaseRepository(purchaseAPI)

        purchaseRepository.pruchaseProcess(perchaseProccessRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
                callback(null)
            }
        }

    }
}