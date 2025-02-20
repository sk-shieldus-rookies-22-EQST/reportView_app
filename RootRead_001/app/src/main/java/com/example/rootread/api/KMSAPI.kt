package com.example.rootread.api

import com.example.rootread.model.kms.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface KMSAPI {

    @POST("generate-presigned-url")
    fun generate(@Body request: GemerateRequest): Call<GenerateResponse>

    @POST("get-key")
    fun getkey(@Body request: GetKeyRequest): Call<GetkeyResponse>
}