package com.example.bookies_001.api

import com.example.bookies_001.model.api.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("api/signup")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>

}