package f0.c.rootread.ui.auth.action

import android.content.Context
import f0.c.rootread.api.AuthAPI
import f0.c.rootread.model.auth.FindPWRequest
import f0.c.rootread.repository.AuthRepository

class FindPWAction(
    private val context: Context,
    private val authApi: AuthAPI
) {
    fun doFindPW(findPWRequest: FindPWRequest, callback: (Boolean?) -> Unit) {
        val findPWRepository = AuthRepository(authApi)

        findPWRepository.findPW(findPWRequest) { response, error ->
            if (response != null) {
                callback(response.status)
            } else {
                error?.printStackTrace()
            }

        }
    }
}