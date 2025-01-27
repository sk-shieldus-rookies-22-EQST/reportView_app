package com.example.reportview_003.ui.purchase.action

import android.content.Context
import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.DeleteItemRequest
import com.example.reportview_003.model.purchase.DeleteItemResponse
import com.example.reportview_003.repository.PurchaseRepository

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