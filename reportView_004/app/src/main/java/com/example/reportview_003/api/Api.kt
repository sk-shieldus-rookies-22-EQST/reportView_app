package com.example.reportview_003.api

import com.example.reportview_003.model.api.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("api/signup")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>

}