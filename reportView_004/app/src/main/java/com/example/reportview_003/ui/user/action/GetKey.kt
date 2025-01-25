package com.example.reportview_003.ui.user.action

import android.content.Context
import com.example.reportview_003.api.KMSAPI
import com.example.reportview_003.model.kms.GetkeyResponse
import com.example.reportview_003.repository.KmsRepository
import retrofit2.Callback

class GetKey(
    private val context: Context,
    private val kmsapi: KMSAPI
) {
    fun getkey(
        callback: (GetkeyResponse?) -> Unit
    ) {
        val kmsRepository = KmsRepository(kmsapi)

        kmsRepository.getkey { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }

    }

}