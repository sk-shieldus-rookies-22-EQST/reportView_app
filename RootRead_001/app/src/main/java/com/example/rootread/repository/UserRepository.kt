package com.example.rootread.repository

import com.example.rootread.api.UserAPI
import com.example.rootread.model.StatusResponse
import com.example.rootread.model.user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val api: UserAPI) {

    fun userInfo(userinfoRequest:UserinfoRequest, callback: (UserinfoResponse?, Throwable?) -> Unit) {
        api.userInfo(userinfoRequest).enqueue(object : Callback<UserinfoResponse>{
            override fun onResponse(call: Call<UserinfoResponse>, response: Response<UserinfoResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<UserinfoResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun userUpdate(userUpdateRequest: UserupdateRequest, callback: (StatusResponse?, Throwable?) -> Unit) {
        api.userUpdate(userUpdateRequest).enqueue(object : Callback<StatusResponse> {
            override fun onResponse(
                call: Call<StatusResponse>,
                response: Response<StatusResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }
            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun userBooklist(userbooklistRequest:UserbooklistRequest, callback: (UserbooklistResponse?, Throwable?) -> Unit) {
        api.userBooklist(userbooklistRequest).enqueue(object : Callback<UserbooklistResponse>{
            override fun onResponse(call: Call<UserbooklistResponse>, response: Response<UserbooklistResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<UserbooklistResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun userPurchase(userpurchaseRequest:UserpurchaseRequest, callback: (UserpurchaseResponse?, Throwable?) -> Unit) {
        api.userPurchase(userpurchaseRequest).enqueue(object : Callback<UserpurchaseResponse>{
            override fun onResponse(call: Call<UserpurchaseResponse>, response: Response<UserpurchaseResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<UserpurchaseResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun signout(signoutRequest: SignoutRequest, callback: (StatusResponse?, Throwable?) -> Unit) {
        api.signout(signoutRequest).enqueue(object : Callback<StatusResponse>{
            override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                callback(response.body(), null)
            }
            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                callback(null,t)
            }
        })
    }
}