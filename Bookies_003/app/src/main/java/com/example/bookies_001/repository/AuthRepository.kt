package com.example.bookies_001.repository

import com.example.bookies_001.api.AuthAPI
import com.example.bookies_001.model.auth.FindIDRequest
import com.example.bookies_001.model.auth.FindIDResponse
import com.example.bookies_001.model.auth.FindPWRequest
import com.example.bookies_001.model.auth.*
import com.example.bookies_001.model.user.UserinfoRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val api: AuthAPI) {

    fun login(loginRequest: LoginRequest, callback: (LoginResponse?, Throwable?) -> Unit) {
        api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
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


    fun findID(findIDRequest: FindIDRequest, callback: (FindIDResponse?, Throwable?) -> Unit) {
        api.findID(findIDRequest).enqueue(object : Callback<FindIDResponse>{
            override fun onResponse(call: Call<FindIDResponse>, response: Response<FindIDResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<FindIDResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun findPW(findPWRequest: FindPWRequest, callback: (FindPWResponse?, Throwable?) -> Unit) {
        api.findPW(findPWRequest).enqueue(object : Callback<FindPWResponse>{
            override fun onResponse(call: Call<FindPWResponse>, response: Response<FindPWResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<FindPWResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun userLevel(userId: String, callback: (UserLevelResponse?) -> Unit) {
        val userLevelRequest = UserinfoRequest(
            user_id = userId
        )
        api.userLevel(userLevelRequest).enqueue(object : Callback<UserLevelResponse> {
            override fun onResponse(
                call: Call<UserLevelResponse>,
                response: Response<UserLevelResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<UserLevelResponse>, t: Throwable) {
                callback(null)
            }
        })
    }

}
