package com.example.bookies_001.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofit: Retrofit by lazy {
        createRetrofit("https://dahaezlge.kro.kr:30303/")
    }

    val KMSretrofit: Retrofit by lazy {
        createRetrofit("http://34.239.180.114:8080/")
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(NetworkClient.client) // ✅ OkHttpClient 사용
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
