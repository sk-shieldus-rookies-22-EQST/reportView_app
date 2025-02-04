package com.example.bookies_001

import android.app.Application
import com.example.bookies_001.utils.AESUtil
import com.example.bookies_001.network.RetrofitClient
import retrofit2.Retrofit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AESUtil.init(this)
    }

    val retrofit: Retrofit
        get() = RetrofitClient.retrofit

    val KMSretrofit: Retrofit
        get() = RetrofitClient.KMSretrofit
}
