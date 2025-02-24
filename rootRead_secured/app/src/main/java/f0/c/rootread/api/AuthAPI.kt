package f0.c.rootread.api

import f0.c.rootread.model.StatusResponse
import f0.c.rootread.model.auth.*
import f0.c.rootread.model.user.UserinfoRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/auth/autologin")
    fun autoLogin(@Body request:TokenRequest): Call<StatusResponse>

    @POST("auth/find/id")
    fun findID(@Body request: FindIDRequest): Call<FindIDResponse>

    @POST("auth/modify/pw")
    fun findPW(@Body request: FindPWRequest): Call<FindPWResponse>

    @POST("auth/user/level")
    fun userLevel(@Body request: UserinfoRequest): Call<UserLevelResponse>
}
