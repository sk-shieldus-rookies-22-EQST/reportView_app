package com.example.reportview_002.repository

import com.example.reportview_002.api.AuthAPI
import com.example.reportview_002.api.LoginRequest
import com.example.reportview_002.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val api: AuthAPI) {

    fun login(username: String, password: String, callback: (LoginResponse?, Throwable?) -> Unit) {
        val request = LoginRequest(username, password)
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

}
