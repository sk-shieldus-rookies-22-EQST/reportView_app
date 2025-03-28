package com.example.bookies_001.api

import com.example.bookies_001.model.kms.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface KMSAPI {

    @POST("generate-presigned-url")
    fun generate(@Body request: GemerateRequest): Call<GenerateResponse>

    @POST("get-key")
    fun getkey(@Body request: GetKeyRequest): Call<GetkeyResponse>

    @POST("mobile-key")
    fun mobileKey(@Body request: MobileKeyRequest): Call<GetkeyResponse>
}