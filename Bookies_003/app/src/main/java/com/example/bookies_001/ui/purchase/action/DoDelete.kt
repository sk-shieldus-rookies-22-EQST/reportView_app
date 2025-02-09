package com.example.bookies_001.ui.purchase.action

import android.content.Context
import com.example.bookies_001.api.PurchaseAPI
import com.example.bookies_001.model.purchase.DeleteItemRequest
import com.example.bookies_001.model.purchase.DeleteItemResponse
import com.example.bookies_001.repository.PurchaseRepository

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