package com.example.bookies_001

import android.app.Application
import com.example.bookies_001.utils.AESUtil
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.cert.X509Certificate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class App : Application() {
    lateinit var retrofit: Retrofit
    lateinit var KMSretrofit: Retrofit
    lateinit var LoadFileretrofit: Retrofit
        private set

    override fun onCreate() {
        super.onCreate()

        // AESUtil 초기화
        AESUtil.init(this)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // SSL 설정 (HTTPS의 경우, 필요 시 사용)
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, trustAllCerts, SecureRandom())
        }

        val sslSocketFactory = sslContext.socketFactory

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectionPool(ConnectionPool(2, 5, TimeUnit.MINUTES))  // 최대 5개의 유휴 커넥션을 5분간 유지
            .connectTimeout(30, TimeUnit.SECONDS)   // 연결 타임아웃 30초
            .readTimeout(60, TimeUnit.SECONDS)      // 읽기 타임아웃 60초 (서버 응답 전체 수신까지 기다림)
            .writeTimeout(60, TimeUnit.SECONDS)     // 쓰기 타임아웃 60초
            .retryOnConnectionFailure(true)         // 연결 실패 시 재시도
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager) // HTTPS 인증 무시 (테스트용)
            .hostnameVerifier { _, _ -> true }       // 모든 호스트 허용 (테스트용)
            .build()

        // Retrofit 인스턴스 생성 (공통 클라이언트 사용)
        retrofit = createRetrofit("https://dahaezlge.kro.kr:30303/", client)
        KMSretrofit = createRetrofit("http://34.239.180.114:8080/", client)

    }

    // 공통 Retrofit 빌더 함수
    private fun createRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}