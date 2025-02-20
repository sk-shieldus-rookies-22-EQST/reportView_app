package com.example.rootread

import android.app.Application
import com.example.rootread.network.RetrofitClient
import com.example.rootread.utils.SessionManager
import retrofit2.Retrofit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (!SessionManager.isAutoLogin(this)){
            SessionManager.clearSession(this)
        }
    }


    val retrofit: Retrofit
        get() = RetrofitClient.retrofit

    val KMSretrofit: Retrofit
        get() = RetrofitClient.KMSretrofit
}
