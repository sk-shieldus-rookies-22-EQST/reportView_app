package com.example.rootread.api

import com.example.rootread.model.auth.*
import com.example.rootread.model.user.UserinfoRequest
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

    @POST("auth/user/level")
    fun userLevel(@Body request: UserinfoRequest): Call<UserLevelResponse>
}
