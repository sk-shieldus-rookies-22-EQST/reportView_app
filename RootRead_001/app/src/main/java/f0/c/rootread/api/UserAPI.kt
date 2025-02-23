package f0.c.rootread.api

import f0.c.rootread.model.StatusResponse
import f0.c.rootread.model.user.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("api/user/info")
    fun userInfo(@Body request: UserinfoRequest): Call<UserinfoResponse>

    @POST("api/user/update")
    fun userUpdate(@Body request: UserupdateRequest): Call<StatusResponse>

    @POST("api/user/booklist")
    fun userBooklist(@Body request: UserbooklistRequest): Call<UserbooklistResponse>

    @POST("api/user/purchase")
    fun userPurchase(@Body request: f0.c.rootread.model.user.UserpurchaseRequest): Call<UserpurchaseResponse>

    @POST("/api/signout")
    fun signout(@Body request: SignoutRequest): Call<StatusResponse>
}