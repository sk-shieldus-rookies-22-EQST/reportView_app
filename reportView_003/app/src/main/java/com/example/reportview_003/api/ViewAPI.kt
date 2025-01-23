package com.example.reportview_003.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

data class viewbooklistResponse(val book_list: MutableList<MutableMap<String,Any>>)

data class viewbooksearchRequest(val keyword: String? = null, val sdate: String?=null, val edate: String?=null, val theme: String?=null)
data class viewbooksearchResponse(val book_list: MutableList<MutableMap<String,Any>>)

data class viewbookdetailResponse(val bookid:Int, val title:String, val writer:String, val detail:String)

data class viewbookviewerRequest(val bookid: Int)
data class viewbookviewerResponse(val status: String)

interface ViewAPI {
    @POST("view/booklist")
    fun viewbooklist(): Call<viewbooklistResponse>

    @POST("view/search")
    fun viewsearch(@Body request: viewbooksearchRequest): Call<viewbooksearchResponse>

    @POST("view/bookdetail/{bookid}")
    fun viewbookdetail(@Path("bookid") id: Int): Call<viewbookdetailResponse>

    @POST("view/book/viewer")
    fun viewbookviewer(@Body request: viewbookviewerRequest): Call<viewbookviewerResponse>
}