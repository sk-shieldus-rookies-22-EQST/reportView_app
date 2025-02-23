package f0.c.rootread.ui.user.action

import android.content.Context
import f0.c.rootread.api.UserAPI
import f0.c.rootread.model.user.UserbooklistRequest
import f0.c.rootread.model.user.UserbooklistResponse
import f0.c.rootread.repository.UserRepository

class GetUserBookList(
    private val context: Context,
    private val userAPI: UserAPI
) {
    fun loadUserBooklist(
        userbooklistRequest: UserbooklistRequest,
        callback: (UserbooklistResponse?) -> Unit
    ) {
        val userRepository = UserRepository(userAPI)

        userRepository.userBooklist(userbooklistRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}