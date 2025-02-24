package f0.c.rootread.ui.auth.action

import android.content.Context
import f0.c.rootread.api.AuthAPI
import f0.c.rootread.model.auth.FindIDRequest
import f0.c.rootread.repository.AuthRepository

class FindIDAction(
    private val context: Context,
    private val authAPI: AuthAPI
) {

    fun doFindid(findIDRequest: FindIDRequest, callback: (String?) -> Unit) {
        val authRepository = AuthRepository(authAPI)

        authRepository.findID(findIDRequest) { response, error ->
            if (response != null) {
                callback(response.user_id)
            } else {
                error?.printStackTrace()
            }

        }
    }
}