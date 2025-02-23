package f0.c.rootread.ui.user.action

import android.content.Context
import f0.c.rootread.api.UserAPI
import f0.c.rootread.model.user.UserpurchaseResponse
import f0.c.rootread.repository.UserRepository

class GetUserPurchase(
    private val context: Context,
    private val userAPI: UserAPI
) {
    fun loadUserPurchase(
        userpurchaseRequest: f0.c.rootread.model.user.UserpurchaseRequest,
        callback: (UserpurchaseResponse?) -> Unit
    ) {
        val userPurchase = UserRepository(userAPI)

        userPurchase.userPurchase(userpurchaseRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}