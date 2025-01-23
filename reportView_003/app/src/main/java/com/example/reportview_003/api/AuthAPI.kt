package com.example.reportview_003.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val userid: String, val passwd: String)
data class LoginResponse(val status: Boolean)

data class findIDRequest(val userid: String, val email: String)
data class findIDResponse(val user_id: String)

data class findPWRequest(val userid: String, val email: String)
data class findPWResponse(val status: String)

interface AuthAPI {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/find/id")
    fun findID(@Body request: findIDRequest): Call<findIDResponse>

    @POST("auth/find/pw")
    fun findPW(@Body request: findPWRequest): Call<findPWResponse>
}
