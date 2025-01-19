package com.example.reportview_002

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    lateinit var retrofit: Retrofit
        private set

    override fun onCreate() {
        super.onCreate()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.187:8000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}