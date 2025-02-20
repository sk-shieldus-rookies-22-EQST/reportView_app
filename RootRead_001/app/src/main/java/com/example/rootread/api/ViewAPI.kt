package com.example.rootread.api

import com.example.rootread.model.view.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ViewAPI {
    @POST("view/booklist")
    fun viewbooklist(): Call<ViewbooklistResponse>

    @POST("view/search")
    fun viewsearch(@Body request: ViewbooksearchRequest): Call<ViewbooksearchResponse>

    @GET("view/bookdetail/{bookid}")
    fun viewbookdetail(@Path("bookid") book_id: String): Call<ViewbookdetailResponse>

    @POST("view/book/viewer")
    fun viewbookviewer(@Body request: ViewbookviewerRequest): Call<ViewbookviewerResponse>
}