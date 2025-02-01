package com.example.reportview_003.api

import com.example.reportview_003.model.auth.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/find/id")
    fun findID(@Body request: FindIDRequest): Call<FindIDResponse>

    @POST("auth/modify/pw")
    fun findPW(@Body request: FindPWRequest): Call<FindPWResponse>
}
