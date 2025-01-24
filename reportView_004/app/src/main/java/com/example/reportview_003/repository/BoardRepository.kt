package com.example.reportview_003.repository

import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardRepository(private val api: BoardAPI) {

    fun render(callback: (BoardResponse?, Throwable?) -> Unit) {
        api.render().enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun showQna(id: Int,callback: (BoardQnAResponse?, Throwable?) -> Unit) {
        api.showQna(id).enqueue(object : Callback<BoardQnAResponse> {
            override fun onResponse(call: Call<BoardQnAResponse>, response: Response<BoardQnAResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<BoardQnAResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun writeQnA(boardWriteRequest:BoardWriteRequest, callback: (BoardWriteResponse?, Throwable?) -> Unit) {
        api.writeQnA(boardWriteRequest).enqueue(object : Callback<BoardWriteResponse>{
            override fun onResponse(call: Call<BoardWriteResponse>, response: Response<BoardWriteResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<BoardWriteResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun writeComment(boardCommentRequest:BoardCommentRequest , callback: (BoardCommentResponse?, Throwable?) -> Unit) {
        api.writeComment(boardCommentRequest).enqueue(object : Callback<BoardCommentResponse>{
            override fun onResponse(call: Call<BoardCommentResponse>, response: Response<BoardCommentResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<BoardCommentResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

}