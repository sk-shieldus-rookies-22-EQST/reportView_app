package f0.c.rootread.ui.purchase.action

import android.content.Context
import f0.c.rootread.api.PurchaseAPI
import f0.c.rootread.model.purchase.DeleteItemRequest
import f0.c.rootread.model.purchase.DeleteItemResponse
import f0.c.rootread.repository.PurchaseRepository

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