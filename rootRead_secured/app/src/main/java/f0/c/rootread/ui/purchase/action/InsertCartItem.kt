package f0.c.rootread.ui.purchase.action

import android.content.Context
import f0.c.rootread.api.PurchaseAPI
import f0.c.rootread.model.StatusResponse
import f0.c.rootread.model.purchase.CartGetItemRequest
import f0.c.rootread.repository.PurchaseRepository

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