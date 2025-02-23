package f0.c.rootread.api

import f0.c.rootread.model.api.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("api/signup")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>
}