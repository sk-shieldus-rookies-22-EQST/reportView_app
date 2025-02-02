package com.example.reportview_003.repository

import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.model.StatusResponse
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

    fun showQna(id: Long,callback: (BoardQnAResponse?, Throwable?) -> Unit) {
        api.showQna(id.toString()).enqueue(object : Callback<BoardQnAResponse> {
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

    fun writeComment(boardCommentRequest:BoardCommentRequest , callback: (StatusResponse?, Throwable?) -> Unit) {
        api.writeComment(boardCommentRequest).enqueue(object : Callback<StatusResponse>{
            override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun modifyQnA(boardModifyRequest:BoardModifyRequest , callback: (BoardModifyResponse?, Throwable?) -> Unit) {
        api.modifyQnA(boardModifyRequest).enqueue(object : Callback<BoardModifyResponse>{
            override fun onResponse(call: Call<BoardModifyResponse>, response: Response<BoardModifyResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }
            override fun onFailure(call: Call<BoardModifyResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun deleteQnA(boardDeleteRequest:BoardDeleteRequest , callback: (BoardDeleteResponse?, Throwable?) -> Unit) {
        api.deleteQnA(boardDeleteRequest).enqueue(object : Callback<BoardDeleteResponse>{
            override fun onResponse(call: Call<BoardDeleteResponse>, response: Response<BoardDeleteResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }
            override fun onFailure(call: Call<BoardDeleteResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

}