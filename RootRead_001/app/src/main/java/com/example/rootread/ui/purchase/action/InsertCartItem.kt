package com.example.rootread.ui.purchase.action

import android.content.Context
import com.example.rootread.api.PurchaseAPI
import com.example.rootread.model.StatusResponse
import com.example.rootread.model.purchase.CartGetItemRequest
import com.example.rootread.repository.PurchaseRepository

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