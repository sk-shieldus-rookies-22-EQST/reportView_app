package com.example.reportview_003.repository

import com.example.reportview_003.api.BoardAPI
import com.example.reportview_003.api.boardCommentRequest
import com.example.reportview_003.api.boardCommentResponse
import com.example.reportview_003.api.boardQnAResponse
import com.example.reportview_003.api.boardResponse
import com.example.reportview_003.api.boardWriteRequest
import com.example.reportview_003.api.boardWriteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardRepository(private val api: BoardAPI) {

    fun render(callback: (boardResponse?, Throwable?) -> Unit) {
        api.render().enqueue(object : Callback<boardResponse> {
            override fun onResponse(call: Call<boardResponse>, response: Response<boardResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<boardResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun showQna(id: Int,callback: (boardQnAResponse?, Throwable?) -> Unit) {
        api.showQna(id).enqueue(object : Callback<boardQnAResponse> {
            override fun onResponse(call: Call<boardQnAResponse>, response: Response<boardQnAResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<boardQnAResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun writeQnA(title: String, content: String ,userid: String, callback: (boardWriteResponse?, Throwable?) -> Unit) {
        val request = boardWriteRequest(title,content,userid)
        api.writeQnA(request).enqueue(object : Callback<boardWriteResponse>{
            override fun onResponse(call: Call<boardWriteResponse>, response: Response<boardWriteResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<boardWriteResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun writeComment(qnaid: Int, comment: String , callback: (boardCommentResponse?, Throwable?) -> Unit) {
        val request = boardCommentRequest(qnaid, comment)
        api.writeComment(request).enqueue(object : Callback<boardCommentResponse>{
            override fun onResponse(call: Call<boardCommentResponse>, response: Response<boardCommentResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<boardCommentResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

}