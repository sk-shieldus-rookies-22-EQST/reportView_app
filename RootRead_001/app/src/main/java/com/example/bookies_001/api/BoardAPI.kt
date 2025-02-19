package com.example.bookies_001.api

import com.example.bookies_001.model.StatusResponse
import com.example.bookies_001.model.board.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface BoardAPI {
    @POST("board/qna")
    fun render(): Call<BoardResponse>

    // API 수정 필요
    @GET("board/qna/{id}")
    fun showQna(@Path("id") id: String): Call<BoardQnAResponse>

    @POST("board/qna/write")
    fun writeQnA(@Body request: BoardWriteRequest): Call<BoardWriteResponse>

    // 기능이 없음 기능 추가 필요
    @POST("board/qna/comment")
    fun writeComment(@Body request: BoardCommentRequest): Call<StatusResponse>

    @POST("board/qna/delete")
    fun deleteQnA(@Body request: BoardDeleteRequest): Call<BoardDeleteResponse>

    @POST("board/qna/modify")
    fun modifyQnA(@Body request: BoardModifyRequest): Call<BoardModifyResponse>

    @Multipart
    @POST("/board/qna/write")
    fun writeQnAMultipart(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("writer") writer: RequestBody,
        @Part file: MultipartBody.Part? // ✅ Nullable로 변경
    ): Call<BoardWriteResponse>


}