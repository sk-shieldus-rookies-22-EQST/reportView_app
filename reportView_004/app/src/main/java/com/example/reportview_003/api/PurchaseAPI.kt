package com.example.reportview_003.api

import com.example.reportview_003.model.StatusResponse
import com.example.reportview_003.model.purchase.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PurchaseAPI {

    @POST("purchase/cart")
    fun purchaseCart(@Body request: CartRequest): Call<CartResponse>

    // 카트 요소 삭제 API 설정 부분 변경 필
    @POST("purchase/delete")
    fun deleteCartItem(@Body request: DeleteItemRequest): Call<DeleteItemResponse>

    @POST("purchase/item")
    fun cartGetItem(@Body request: CartGetItemRequest): Call<StatusResponse>

    @POST("purchase/process")
    fun pruchaseProcess(@Body request: PerchaseProccessRequest): Call<PerchaseProccessResponse>

    @POST("purchase/point")
    fun chargePoint(@Body request: ChargePointRequest): Call<StatusResponse>

}