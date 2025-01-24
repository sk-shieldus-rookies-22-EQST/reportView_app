package com.example.reportview_003.repository

import com.example.reportview_003.api.UserAPI
import com.example.reportview_003.model.user.*
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
}