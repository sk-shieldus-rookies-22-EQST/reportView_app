package com.example.reportview_003.repository

import com.example.reportview_003.api.UserAPI
import com.example.reportview_003.api.userbooklistRequest
import com.example.reportview_003.api.userbooklistResponse
import com.example.reportview_003.api.userinfoRequest
import com.example.reportview_003.api.userinfoResponse
import com.example.reportview_003.api.userpurchaseRequest
import com.example.reportview_003.api.userpurchaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(private val api: UserAPI) {

    fun userInfo(userid: String, callback: (userinfoResponse?, Throwable?) -> Unit) {
        val request = userinfoRequest(userid)
        api.userInfo(request).enqueue(object : Callback<userinfoResponse>{
            override fun onResponse(call: Call<userinfoResponse>, response: Response<userinfoResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<userinfoResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun userBooklist(userid: String, callback: (userbooklistResponse?, Throwable?) -> Unit) {
        val request = userbooklistRequest(userid)
        api.userBooklist(request).enqueue(object : Callback<userbooklistResponse>{
            override fun onResponse(call: Call<userbooklistResponse>, response: Response<userbooklistResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<userbooklistResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun userPurchase(userid: String, callback: (userpurchaseResponse?, Throwable?) -> Unit) {
        val request = userpurchaseRequest(userid)
        api.userPurchase(request).enqueue(object : Callback<userpurchaseResponse>{
            override fun onResponse(call: Call<userpurchaseResponse>, response: Response<userpurchaseResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<userpurchaseResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}