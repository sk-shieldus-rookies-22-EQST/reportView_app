package com.example.rootread.repository

import com.example.rootread.api.Api
import com.example.rootread.model.api.SignupRequest
import com.example.rootread.model.api.SignupResponse
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
