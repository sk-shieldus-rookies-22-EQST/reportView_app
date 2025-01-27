package com.example.reportview_003.ui.purchase.action

import android.content.Context
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.StatusResponse
import com.example.reportview_003.model.purchase.CartGetItemRequest
import com.example.reportview_003.repository.PurchaseRepository

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