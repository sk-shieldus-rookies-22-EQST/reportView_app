package f0.c.rootread.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val retrofit: Retrofit by lazy {
        createRetrofit("https://ebook.sas1.n-e.kr/")
    }

    val KMSretrofit: Retrofit by lazy {
        createRetrofit("http://ebook.sas1.n-e.kr:8080/")
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
