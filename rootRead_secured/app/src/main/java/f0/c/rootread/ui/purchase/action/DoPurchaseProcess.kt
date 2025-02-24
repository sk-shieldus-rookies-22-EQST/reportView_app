package f0.c.rootread.ui.purchase.action

import f0.c.rootread.api.PurchaseAPI
import f0.c.rootread.model.purchase.*
import f0.c.rootread.repository.PurchaseRepository

class DoPurchaseProcess(
    private val purchaseAPI: PurchaseAPI
) {
    fun doPurchaseProcess(
        perchaseProccessRequest: PerchaseProccessRequest,
        callback: (PerchaseProccessResponse?) -> Unit) {
        val purchaseRepository = PurchaseRepository(purchaseAPI)

        purchaseRepository.pruchaseProcess(perchaseProccessRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
                callback(null)
            }
        }

    }
}