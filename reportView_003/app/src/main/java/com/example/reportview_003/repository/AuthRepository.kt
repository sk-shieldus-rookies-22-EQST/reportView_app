package com.example.reportview_003.repository

import com.example.reportview_003.api.AuthAPI
import com.example.reportview_003.api.LoginRequest
import com.example.reportview_003.api.LoginResponse
import com.example.reportview_003.api.findIDRequest
import com.example.reportview_003.api.findIDResponse
import com.example.reportview_003.api.findPWRequest
import com.example.reportview_003.api.findPWResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val api: AuthAPI) {

    fun login(userid: String, passwd: String, callback: (LoginResponse?, Throwable?) -> Unit) {
        val request = LoginRequest(userid, passwd)
        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Login failed"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }


    fun findID(userid: String, email: String, callback: (findIDResponse?, Throwable?) -> Unit) {
        val request = findIDRequest(userid, email)
        api.findID(request).enqueue(object : Callback<findIDResponse>{
            override fun onResponse(call: Call<findIDResponse>, response: Response<findIDResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<findIDResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun findPW(userid: String, email: String, callback: (findPWResponse?, Throwable?) -> Unit) {
        val request = findPWRequest(userid, email)
        api.findPW(request).enqueue(object : Callback<findPWResponse>{
            override fun onResponse(call: Call<findPWResponse>, response: Response<findPWResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<findPWResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }


}
