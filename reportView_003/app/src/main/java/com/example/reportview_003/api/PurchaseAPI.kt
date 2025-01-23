package com.example.reportview_003.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class cartRequest(val bookid: List<Int>)
data class cartResponse(val book_list: MutableList<MutableMap<String,Any>>, val totalprice: String, val cartid: Int)

data class perchaseProccessRequest(val userid: String, val bookid: Int, val totalprice: String)
data class perchaseProccessResponse(val status: String)

interface PurchaseAPI {

    @POST("purchase/cart")
    fun purchaseCart(@Body request: cartRequest): Call<cartResponse>

    @POST("purchase/process")
    fun pruchaseProcess(@Body request: perchaseProccessRequest): Call<perchaseProccessResponse>
}