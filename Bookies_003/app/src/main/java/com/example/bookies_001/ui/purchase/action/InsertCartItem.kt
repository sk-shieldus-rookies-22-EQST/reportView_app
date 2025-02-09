package com.example.bookies_001.ui.purchase.action

import android.content.Context
import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.model.StatusResponse
import com.example.bookies_001.model.purchase.CartGetItemRequest
import com.example.bookies_001.repository.PurchaseRepository

class InsertCartItem(
    private val context: Context,
    private val purchaseAPI: PurchaseAPI
) {
    fun insertCartItem(cartGetItemRequest: CartGetItemRequest, callback: (StatusResponse?) -> Unit) {
        val purchaseRepository = PurchaseRepository(purchaseAPI)
        purchaseRepository.cartGetItem(cartGetItemRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}