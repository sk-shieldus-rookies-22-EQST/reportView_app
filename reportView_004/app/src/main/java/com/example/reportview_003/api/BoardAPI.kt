package com.example.reportview_003.api

import com.example.reportview_003.model.StatusResponse
import com.example.reportview_003.model.board.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BoardAPI {
    @GET("board/qna")
    fun render(): Call<BoardResponse>

    // API 수정 필요
    @GET("board/qna/{id}")
    fun showQna(@Path("id") id: Int): Call<BoardQnAResponse>

    @POST("board/qna/write")
    fun writeQnA(@Body request: BoardWriteRequest): Call<BoardWriteResponse>

    // 기능이 없음 기능 추가 필요
    @POST("board/qna/comment")
    fun writeComment(@Body request: BoardCommentRequest): Call<StatusResponse>

    @POST("board/delete")
    fun deleteQnA(@Body request: BoardDeleteRequest): Call<BoardDeleteResponse>

    @POST("board/modify")
    fun modifyQnA(@Body request: BoardModifyRequest): Call<BoardModifyResponse>
}