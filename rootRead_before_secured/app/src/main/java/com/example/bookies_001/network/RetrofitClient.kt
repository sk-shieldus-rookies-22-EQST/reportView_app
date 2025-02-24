package com.example.bookies_001.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofit: Retrofit by lazy {
        createRetrofit("https://3.35.84.46/")
    }

    val KMSretrofit: Retrofit by lazy {
        createRetrofit("http://3.35.84.46:8080/")
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(NetworkClient.client) // ✅ OkHttpClient 사용
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
