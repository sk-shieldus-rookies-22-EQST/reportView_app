package com.example.reportview_003.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

data class boardResponse(val qna: MutableList<MutableMap<String,Any>>)

data class boardQnAResponse(val id: Int, val content:String , val comment:String)

data class boardWriteRequest(val title: String, val content: String, val userID: String)
data class boardWriteResponse(val status: String)

data class boardCommentRequest(val qnaid: Int, val comment: String)
data class boardCommentResponse(val status: String)

interface BoardAPI {
    @POST("board/qna")
    fun render(): Call<boardResponse>

    @POST("board/qna/{id}")
    fun showQna(@Path("id") id: Int): Call<boardQnAResponse>

    @POST("board/qna/write")
    fun writeQnA(@Body request: boardWriteRequest): Call<boardWriteResponse>

    @POST("board/qna/comment")
    fun writeComment(@Body request: boardCommentRequest): Call<boardCommentResponse>
}