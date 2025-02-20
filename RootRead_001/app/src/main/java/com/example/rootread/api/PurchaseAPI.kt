package com.example.rootread.api

import com.example.rootread.model.StatusResponse
import com.example.rootread.model.purchase.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PurchaseAPI {

    // 서버 측 API 확인 필요
    @POST("api/purchase/cart")
    fun purchaseCart(@Body request: CartRequest): Call<CartResponse>

    // 카트 요소 삭제 API 설정 부분 변경 필
    @POST("api/purchase/delete")
    fun deleteCartItem(@Body request: DeleteItemRequest): Call<DeleteItemResponse>

    @POST("api/purchase/add")
    fun cartGetItem(@Body request: CartGetItemRequest): Call<StatusResponse>

    @POST("api/purchase/process")
    fun pruchaseProcess(@Body request: PerchaseProccessRequest): Call<PerchaseProccessResponse>

    @POST("api/user/point/charge")
    fun chargePoint(@Body request: ChargePointRequest): Call<ChargePointResponse>

    @POST("api/user/point")
    fun userPoint(@Body request: UserpointRequest): Call<UserpointResponse>
}