package com.example.rootread.ui.purchase.action

import com.example.rootread.api.PurchaseAPI
import com.example.rootread.model.purchase.*
import com.example.rootread.repository.PurchaseRepository

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