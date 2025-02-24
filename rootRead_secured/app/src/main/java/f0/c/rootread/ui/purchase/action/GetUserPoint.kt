package f0.c.rootread.ui.purchase.action

import f0.c.rootread.api.PurchaseAPI
import f0.c.rootread.model.purchase.UserpointRequest
import f0.c.rootread.model.purchase.UserpointResponse
import f0.c.rootread.repository.PurchaseRepository

class GetUserPoint(
    private val purchaseAPI: PurchaseAPI
) {
    fun getUserPoint(
        userpointRequest: UserpointRequest,
        callback: (UserpointResponse?, Throwable?) -> Unit
    ) {
        val perchaseRepository = PurchaseRepository(purchaseAPI)
        perchaseRepository.userPoint(userpointRequest) { response, error ->
            if (response != null) {
                callback(response, null)
            } else {
                callback(null, Throwable("failed"))
            }
        }
    }
}
