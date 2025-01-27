package com.example.reportview_003.repository

import com.example.reportview_003.api.PurchaseAPI
import com.example.reportview_003.model.StatusResponse
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

    fun deleteFromCart(deleteItemRequest:DeleteItemRequest, callback: (DeleteItemResponse?, Throwable?) -> Unit) {
        api.deleteCartItem(deleteItemRequest).enqueue(object : Callback<DeleteItemResponse> {
            override fun onResponse(call: Call<DeleteItemResponse>, response: Response<DeleteItemResponse>) {
                if (response.isSuccessful) {
                    // 성공 시 body 전달
                    callback(response.body(), null)
                } else {
                    // 실패 시 상세 에러 메시지 전달
                    val errorMsg = response.errorBody()?.string() ?: "Failed with unknown error"
                    callback(null, Throwable(errorMsg))
                }
            }
            override fun onFailure(call: Call<DeleteItemResponse>, t: Throwable) {
                // 네트워크 실패 또는 기타 오류 처리
                callback(null, Throwable(t.localizedMessage ?: "Unknown failure"))
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

    fun cartGetItem(cartGetItemRequest:CartGetItemRequest, callback: (StatusResponse?, Throwable?) -> Unit) {
        api.cartGetItem(cartGetItemRequest).enqueue(object : Callback<StatusResponse> {
            override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                if (response.isSuccessful) {
                    // 성공 시 body 전달
                    callback(response.body(), null)
                } else {
                    // 실패 시 상세 에러 메시지 전달
                    val errorMsg = response.errorBody()?.string() ?: "Failed with unknown error"
                    callback(null, Throwable(errorMsg))
                }
            }
            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                // 네트워크 실패 또는 기타 오류 처리
                callback(null, Throwable(t.localizedMessage ?: "Unknown failure"))
            }
        })
    }

    fun chargePoint(chargePointRequest:ChargePointRequest, callback: (StatusResponse?,Throwable?) -> Unit) {
        api.chargePoint(chargePointRequest).enqueue(object : Callback<StatusResponse> {
            override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                if (response.isSuccessful) {
                    // 성공 시 body 전달
                    callback(response.body(), null)
                } else {
                    // 실패 시 상세 에러 메시지 전달
                    val errorMsg = response.errorBody()?.string() ?: "Failed with unknown error"
                    callback(null, Throwable(errorMsg))
                }
            }
            override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                // 네트워크 실패 또는 기타 오류 처리
                callback(null, Throwable(t.localizedMessage ?: "Unknown failure"))
            }
        })
    }
}