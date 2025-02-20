package com.example.rootread.ui.user.action

import android.content.Context
import com.example.rootread.api.UserAPI
import com.example.rootread.model.StatusResponse
import com.example.rootread.model.user.SignoutRequest
import com.example.rootread.model.user.UserinfoRequest
import com.example.rootread.model.user.UserupdateRequest
import com.example.rootread.repository.UserRepository

class GetUserinfo (
    private val context: Context,
    private val userAPI: UserAPI,
) {
    val userText = UserRepository(userAPI)

    fun loadUserinfo(userid:String,callback: (String?) -> Unit) {

        val userinfoData = UserinfoRequest(
            user_id = userid
        )

        userText.userInfo(userinfoData) { response, error ->
            if (response != null) {
                callback(response.user_id)
            } else {
                error?.printStackTrace()
                callback(null)
            }
        }
    }

    fun updateUserInfo(userUpdateRequest: UserupdateRequest, callback: (Boolean) -> Unit) {
        userText.userUpdate(userUpdateRequest) { response, error ->
            if (response != null) {
                callback(response.status)
            } else {
                error?.printStackTrace()
            }
        }
    }

    fun signoutUser(signoutRequest: SignoutRequest, callback: (StatusResponse) -> Unit) {
        userText.signout(signoutRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}