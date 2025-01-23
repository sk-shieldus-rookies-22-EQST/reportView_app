package com.example.reportview_003.repository

import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.api.cartRequest
import com.example.reportview_003.api.cartResponse
import com.example.reportview_003.api.perchaseProccessRequest
import com.example.reportview_003.api.perchaseProccessResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PurchaseRepository(private val api: PurchaseAPI) {

    fun purchaseCart(bookid: List<Int>, callback: (cartResponse?, Throwable?) -> Unit) {
        val request = cartRequest(bookid)
        api.purchaseCart(request).enqueue(object : Callback<cartResponse>{
            override fun onResponse(call: Call<cartResponse>, response: Response<cartResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<cartResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun pruchaseProcess(userid: String, bookid: Int, totalprice: String, callback: (perchaseProccessResponse?, Throwable?) -> Unit) {
        val request = perchaseProccessRequest(userid, bookid, totalprice)
        api.pruchaseProcess(request).enqueue(object : Callback<perchaseProccessResponse>{
            override fun onResponse(call: Call<perchaseProccessResponse>, response: Response<perchaseProccessResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<perchaseProccessResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}