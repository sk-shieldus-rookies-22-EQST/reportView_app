package f0.c.rootread.ui.auth.action

import android.content.Context
import f0.c.rootread.api.Api
import f0.c.rootread.model.api.SignupRequest
import f0.c.rootread.repository.SignupRepository

class SignupAction(
    private val context: Context,
    private val api: Api
) {

    fun doSignup(signupRequest: SignupRequest, callback: (Boolean?) -> Unit) {
        val signupRepository = SignupRepository(api)

        signupRepository.signup(signupRequest) { response, error ->
            if (response != null) {
                callback(response.status)
            } else {
                error?.printStackTrace()
            }

        }
    }
}