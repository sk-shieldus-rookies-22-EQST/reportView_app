package com.example.reportview_003.ui.purchase.action

import android.content.Context
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.CartRequest
import com.example.reportview_003.model.purchase.CartResponse
import com.example.reportview_003.repository.PurchaseRepository

class GetPurchaseCart(
    private val context: Context,
    private val purchaseAPI: PurchaseAPI
) {
    fun loadPurchaseCart(bookid:MutableList<Int>,callback: (CartResponse?) -> Unit) {
        val purchaseCart = PurchaseRepository(purchaseAPI)

        val cartRequest = CartRequest(
            bookid = bookid
        )

        purchaseCart.purchaseCart(cartRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}