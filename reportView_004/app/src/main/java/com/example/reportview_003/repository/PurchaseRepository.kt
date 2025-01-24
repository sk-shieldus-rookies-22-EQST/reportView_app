package com.example.reportview_003.repository

import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.purchase.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PurchaseRepository(private val api: PurchaseAPI) {

    fun purchaseCart(cartRequest:CartRequest, callback: (CartResponse?, Throwable?) -> Unit) {
        api.purchaseCart(cartRequest).enqueue(object : Callback<CartResponse>{
            override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun deleteFromCart(bookId: deleteCartItemRequest, callback: (Boolean, Throwable?) -> Unit) {
        api.deleteCartItem(bookId).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, Exception("Failed to delete item"))
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                callback(false, t)
            }
        })
    }

    fun pruchaseProcess(perchaseProcessRequest:PerchaseProccessRequest, callback: (PerchaseProccessResponse?, Throwable?) -> Unit) {
        api.pruchaseProcess(perchaseProcessRequest).enqueue(object : Callback<PerchaseProccessResponse>{
            override fun onResponse(call: Call<PerchaseProccessResponse>, response: Response<PerchaseProccessResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("failed"))
                }
            }

            override fun onFailure(call: Call<PerchaseProccessResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}