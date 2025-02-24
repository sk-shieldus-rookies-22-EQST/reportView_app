package f0.c.rootread.ui.purchase.action

import android.content.Context
import f0.c.rootread.api.PurchaseAPI
import f0.c.rootread.model.purchase.CartRequest
import f0.c.rootread.repository.PurchaseRepository

class GetPurchaseCart(
    private val context: Context,
    private val purchaseAPI: PurchaseAPI
) {
    fun loadPurchaseCart(cartRequeste:CartRequest,callback: (f0.c.rootread.model.purchase.CartResponse?) -> Unit) {
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