package f0.c.rootread.repository

import f0.c.rootread.api.KMSAPI
import f0.c.rootread.model.kms.GemerateRequest
import f0.c.rootread.model.kms.GetKeyRequest
import f0.c.rootread.model.kms.MobileKeyRequest
import f0.c.rootread.model.kms.GetkeyResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KmsRepository(private val api: KMSAPI) {

    fun generate(gemerateRequest: GemerateRequest, callback: (f0.c.rootread.model.kms.GenerateResponse?, String?) -> Unit) {
        api.generate(gemerateRequest).enqueue(object : Callback<f0.c.rootread.model.kms.GenerateResponse> {
            override fun onResponse(
                call: Call<f0.c.rootread.model.kms.GenerateResponse>,
                response: Response<f0.c.rootread.model.kms.GenerateResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(),null)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody).getString("error") // ✅ "error" 키의 값 가져오기
                    } catch (e: Exception) {
                        "알 수 없는 오류가 발생했습니다." // ✅ JSON 파싱 실패 시 기본 메시지 반환
                    }
                    callback(null,errorMessage)
                }
            }
            override fun onFailure(call: Call<f0.c.rootread.model.kms.GenerateResponse>, t: Throwable) {
                callback(null, t.message ?: "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun mobileKey(mobileKeyRequest: MobileKeyRequest, callback: (GetkeyResponse?, Throwable?) -> Unit) {
        api.mobileKey(mobileKeyRequest).enqueue(object : Callback<GetkeyResponse> {
            override fun onResponse(
                call: Call<GetkeyResponse>,
                response: Response<GetkeyResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Login failed"))
                }
            }
            override fun onFailure(call: Call<GetkeyResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }

    fun getkey(getKeyRequest: GetKeyRequest, callback: (GetkeyResponse?, Throwable?) -> Unit) {
        api.getkey(getKeyRequest).enqueue(object : Callback<GetkeyResponse> {
            override fun onResponse(
                call: Call<GetkeyResponse>,
                response: Response<GetkeyResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, Throwable("Login failed"))
                }
            }
            override fun onFailure(call: Call<GetkeyResponse>, t: Throwable) {
                callback(null, t)
            }
        })
    }
}