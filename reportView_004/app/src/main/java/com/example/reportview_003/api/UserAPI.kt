package com.example.reportview_003.api

import com.example.reportview_003.model.StatusResponse
import com.example.reportview_003.model.user.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("user/info")
    fun userInfo(@Body request: UserinfoRequest): Call<UserinfoResponse>

    @POST("user/update")
    fun userUpdate(@Body request: UserupdateRequest): Call<StatusResponse>

    @POST("user/booklist")
    fun userBooklist(@Body request: UserbooklistRequest): Call<UserbooklistResponse>

    @POST("user/purchase")
    fun userPurchase(@Body request: UserpurchaseRequest): Call<UserpurchaseResponse>

}