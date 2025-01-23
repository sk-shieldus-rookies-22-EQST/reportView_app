package com.example.reportview_003

import android.app.Application
import com.example.reportview_003.api.ViewAPI
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
            .baseUrl("http://192.168.14.34:8000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}