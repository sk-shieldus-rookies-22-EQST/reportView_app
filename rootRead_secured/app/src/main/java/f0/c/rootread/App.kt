package f0.c.rootread

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import f0.c.rootread.api.AuthAPI
import f0.c.rootread.model.auth.TokenRequest
import f0.c.rootread.network.RetrofitClient
import f0.c.rootread.repository.AuthRepository
import f0.c.rootread.utils.SessionManager
import retrofit2.Retrofit

class App : Application() {

    val retrofit: Retrofit
        get() = RetrofitClient.retrofit

    val KMSretrofit: Retrofit
        get() = RetrofitClient.KMSretrofit

    override fun onCreate() {
        super.onCreate()
        if (!SessionManager.isAutoLogin(this)){
            SessionManager.clearSession(this)
        } else {
            val authAPI = retrofit.create(AuthAPI::class.java)
            val token = TokenRequest(
                user_id = SessionManager.getUserID(this).toString(),
                token = SessionManager.getUserToken(this).toString(),
                uuid = SessionManager.getUUID(this).toString()
            )
            AuthRepository(authAPI).autoLogin(token) { response, error ->
                if (response != null) {
                    if (!response.status){
                        SessionManager.clearSession(this)
                        Toast.makeText(this,"자동 로그인이 해제되었습니다.",Toast.LENGTH_SHORT).show()
                        LocalBroadcastManager.getInstance(this)
                            .sendBroadcast(Intent("UPDATE_NAVIGATION_MENU"))
                    }
                } else {
                    SessionManager.clearSession(this)
                    Toast.makeText(this,"자동 로그인이 해제되었습니다.",Toast.LENGTH_SHORT).show()
                    LocalBroadcastManager.getInstance(this)
                        .sendBroadcast(Intent("UPDATE_NAVIGATION_MENU"))
                }
            }
        }
    }
}
