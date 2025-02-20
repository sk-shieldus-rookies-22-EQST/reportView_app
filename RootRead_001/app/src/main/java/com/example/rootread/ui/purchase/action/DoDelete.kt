package com.example.rootread.ui.purchase.action

import android.content.Context
import com.example.rootread.api.PurchaseAPI
import com.example.rootread.model.purchase.DeleteItemRequest
import com.example.rootread.model.purchase.DeleteItemResponse
import com.example.rootread.repository.PurchaseRepository

class DoDelete(
    private val context: Context,
    private val purchaseApi: PurchaseAPI
) {
    fun doDelet(
        deleteItemRequest: DeleteItemRequest,
        callback: (DeleteItemResponse?) -> Unit
    ) {
        val purchaseRepository = PurchaseRepository(purchaseApi)

        purchaseRepository.deleteFromCart(deleteItemRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
                callback(null)
            }
        }

    }
}