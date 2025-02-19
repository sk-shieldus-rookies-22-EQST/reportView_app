package com.example.bookies_001.repository

import com.example.bookies_001.api.Api
import com.example.bookies_001.model.api.SignupRequest
import com.example.bookies_001.model.api.SignupResponse
import com.example.bookies_001.model.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupRepository(private val api: Api) {

    fun signup(signupRequest: SignupRequest, callback: (SignupResponse?, Throwable?) -> Unit) {
        val request = SignupRequest(
            signupRequest.user_id,
            signupRequest.user_pw,
            signupRequest.user_phone,
            signupRequest.user_email
        )
        api.signup(request).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Login failed"))
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}
