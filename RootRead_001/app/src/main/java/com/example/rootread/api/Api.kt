package com.example.rootread.api

import com.example.rootread.model.api.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("api/signup")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>
}