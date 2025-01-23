package com.example.reportview_003.repository

import com.example.reportview_003.api.ViewAPI
import com.example.reportview_003.api.viewbookdetailResponse
import com.example.reportview_003.api.viewbooklistResponse
import com.example.reportview_003.api.viewbooksearchRequest
import com.example.reportview_003.api.viewbooksearchResponse
import com.example.reportview_003.api.viewbookviewerRequest
import com.example.reportview_003.api.viewbookviewerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewBooklist(private val api: ViewAPI) {

    fun viewbooklist(
        callback: (viewbooklistResponse?, Throwable?) -> Unit) {
        api.viewbooklist().enqueue(object : Callback<viewbooklistResponse> {
            override fun onResponse(
                call: Call<viewbooklistResponse>,
                response: Response<viewbooklistResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Request failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<viewbooklistResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun viewsearch(keyword:String, sdate:String, edate:String, theme: String, callback: (viewbooksearchResponse?, Throwable?) -> Unit) {
        val request = viewbooksearchRequest(keyword,sdate,edate,theme)
        api.viewsearch(request).enqueue(object : Callback<viewbooksearchResponse> {
            override fun onResponse(call: Call<viewbooksearchResponse>, response: Response<viewbooksearchResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<viewbooksearchResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun viewbookdetail(bookid:Int, callback: (viewbookdetailResponse?, Throwable?) -> Unit) {
        api.viewbookdetail(bookid).enqueue(object : Callback<viewbookdetailResponse> {
            override fun onResponse(call: Call<viewbookdetailResponse>, response: Response<viewbookdetailResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<viewbookdetailResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun viewbookviewer(bookid:Int, callback: (viewbookviewerResponse?, Throwable?) -> Unit) {
        val request = viewbookviewerRequest(bookid)
        api.viewbookviewer(request).enqueue(object : Callback<viewbookviewerResponse> {
            override fun onResponse(call: Call<viewbookviewerResponse>, response: Response<viewbookviewerResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Render failed with status: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<viewbookviewerResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}