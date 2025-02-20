package com.example.rootread.ui.purchase.action

import android.content.Context
import com.example.rootread.api.PurchaseAPI
import com.example.rootread.model.purchase.CartRequest
import com.example.rootread.model.purchase.CartResponse
import com.example.rootread.repository.PurchaseRepository

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