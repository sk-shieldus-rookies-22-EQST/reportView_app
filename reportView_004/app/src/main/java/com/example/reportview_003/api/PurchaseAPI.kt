package com.example.reportview_003.api

import com.example.reportview_003.model.purchase.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface PurchaseAPI {

    @POST("purchase/cart")
    fun purchaseCart(@Body request: CartRequest): Call<CartResponse>

    // 카트 요소 삭제 API 설정 부분 변경 필
    @POST("purchase/cart/{id}")
    fun deleteCartItem(@Path("id") @Body request: deleteCartItemRequest): Call<Void>

    @POST("purchase/process")
    fun pruchaseProcess(@Body request: PerchaseProccessRequest): Call<PerchaseProccessResponse>
}