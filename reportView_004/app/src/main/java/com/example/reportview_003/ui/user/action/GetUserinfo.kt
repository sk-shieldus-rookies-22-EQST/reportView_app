package com.example.reportview_003.ui.user.action

import android.content.Context
import com.example.reportview_003.api.UserAPI
import com.example.reportview_003.model.user.UserinfoRequest
import com.example.reportview_003.repository.UserRepository

class GetUserinfo (
    private val context: Context,
    private val userAPI: UserAPI,
) {
    fun loadUserinfo(userid:String,callback: (String?) -> Unit) {
        val userText = UserRepository(userAPI)

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
}