package f0.c.rootread.api

import f0.c.rootread.model.kms.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface KMSAPI {

    @POST("generate-presigned-url")
    fun generate(@Body request: GemerateRequest): Call<GenerateResponse>

    @POST("get-key")
    fun getkey(@Body request: GetKeyRequest): Call<GetkeyResponse>

    @POST("mobile-key")
    fun mobileKey(@Body request: MobileKeyRequest): Call<GetkeyResponse>
}