package com.example.reportview_003.api

import com.example.reportview_003.model.StatusResponse
import com.example.reportview_003.model.user.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("api/user/info")
    fun userInfo(@Body request: UserinfoRequest): Call<UserinfoResponse>

    @POST("api/user/update")
    fun userUpdate(@Body request: UserupdateRequest): Call<StatusResponse>

    @POST("api/user/booklist")
    fun userBooklist(@Body request: UserbooklistRequest): Call<UserbooklistResponse>

    @POST("api/user/purchase")
    fun userPurchase(@Body request: UserpurchaseRequest): Call<UserpurchaseResponse>
}