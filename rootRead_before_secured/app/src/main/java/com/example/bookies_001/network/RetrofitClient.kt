package com.example.bookies_001.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofit: Retrofit by lazy {
        createRetrofit("https://ebook.sas2.n-e.kr/")
    }

    val KMSretrofit: Retrofit by lazy {
        createRetrofit("http://ebook.sas2.n-e.kr:8080/")
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(NetworkClient.client) // ✅ OkHttpClient 사용
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
