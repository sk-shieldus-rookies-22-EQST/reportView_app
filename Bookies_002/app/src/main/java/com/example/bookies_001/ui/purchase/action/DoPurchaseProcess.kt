package com.example.bookies_001.ui.purchase.action

import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.model.purchase.*
import com.example.bookies_001.repository.PurchaseRepository

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