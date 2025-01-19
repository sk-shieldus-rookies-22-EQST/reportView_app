package com.example.reportview_002.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val token: String, val login: String)

interface AuthAPI {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
