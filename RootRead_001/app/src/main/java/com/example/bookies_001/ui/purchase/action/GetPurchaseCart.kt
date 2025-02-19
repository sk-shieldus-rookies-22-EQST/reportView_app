package com.example.bookies_001.ui.purchase.action

import android.content.Context
import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.model.purchase.CartRequest
import com.example.bookies_001.model.purchase.CartResponse
import com.example.bookies_001.repository.PurchaseRepository

class GetPurchaseCart(
    private val context: Context,
    private val purchaseAPI: PurchaseAPI
) {
    fun loadPurchaseCart(cartRequeste:CartRequest,callback: (CartResponse?) -> Unit) {
        val purchaseCart = PurchaseRepository(purchaseAPI)


        purchaseCart.purchaseCart(cartRequeste) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}