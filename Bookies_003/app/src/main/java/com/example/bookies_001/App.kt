package com.example.bookies_001

import android.app.Application
import android.content.Intent
import com.example.bookies_001.utils.AESUtil
import com.example.bookies_001.network.RetrofitClient
import com.example.bookies_001.utils.SessionManager
import retrofit2.Retrofit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AESUtil.init(this)
        if (!SessionManager.isAutoLogin(this)){
            SessionManager.init(this)
        }
    }


    val retrofit: Retrofit
        get() = RetrofitClient.retrofit

    val KMSretrofit: Retrofit
        get() = RetrofitClient.KMSretrofit
}
