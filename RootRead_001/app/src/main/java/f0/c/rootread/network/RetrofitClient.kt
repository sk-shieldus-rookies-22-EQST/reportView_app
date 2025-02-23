package f0.c.rootread.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofit: Retrofit by lazy {
        createRetrofit("https://3.35.84.46/")
    }

    val KMSretrofit: Retrofit by lazy {
        createRetrofit("http://3.35.84.46:8081/")
    }

    val gson: Gson = GsonBuilder()
        .setLenient() // JSON 구조가 엄격하지 않아도 허용
        .create()

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(NetworkClient.client) // ✅ OkHttpClient 사용
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
