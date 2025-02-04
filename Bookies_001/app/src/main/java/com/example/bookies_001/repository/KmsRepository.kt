package com.example.bookies_001.repository

import com.example.bookies_001.api.KMSAPI
import com.example.bookies_001.model.kms.GemerateRequest
import com.example.bookies_001.model.kms.GenerateResponse
import com.example.bookies_001.model.kms.GetkeyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KmsRepository(private val api: KMSAPI) {

    fun generate(gemerateRequest: GemerateRequest, callback: (GenerateResponse?, Throwable?) -> Unit) {
        api.generate(gemerateRequest).enqueue(object : Callback<GenerateResponse> {
            override fun onResponse(
                call: Call<GenerateResponse>,
                response: Response<GenerateResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Login failed"))
                }
            }
            override fun onFailure(call: Call<GenerateResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun getkey(callback: (GetkeyResponse?, Throwable?) -> Unit) {
        api.getkey().enqueue(object : Callback<GetkeyResponse> {
            override fun onResponse(
                call: Call<GetkeyResponse>,
                response: Response<GetkeyResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Login failed"))
                }
            }
            override fun onFailure(call: Call<GetkeyResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}