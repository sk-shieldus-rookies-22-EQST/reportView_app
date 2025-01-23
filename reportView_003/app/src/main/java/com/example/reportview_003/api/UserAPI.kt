package com.example.reportview_003.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class userinfoRequest(val userid: String)
data class userinfoResponse(val user_id: String)

data class userbooklistRequest(val userid: String)
data class userbooklistResponse(val book: MutableList<MutableMap<String,Any>>)

data class userpurchaseRequest(val userid: String)
data class userpurchaseResponse(val purchase: MutableList<MutableMap<String,Any>>)

interface UserAPI {

    @POST("user/info")
    fun userInfo(@Body request: userinfoRequest): Call<userinfoResponse>

    @POST("user/booklist")
    fun userBooklist(@Body request: userbooklistRequest): Call<userbooklistResponse>

    @POST("user/purchase")
    fun userPurchase(@Body request: userpurchaseRequest): Call<userpurchaseResponse>

}