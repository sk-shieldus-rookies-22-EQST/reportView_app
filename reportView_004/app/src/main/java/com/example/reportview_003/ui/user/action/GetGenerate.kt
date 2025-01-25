package com.example.reportview_003.ui.user.action

import android.content.Context
import com.example.reportview_003.api.KMSAPI
import com.example.reportview_003.model.kms.GemerateRequest
import com.example.reportview_003.model.kms.GenerateResponse
import com.example.reportview_003.repository.KmsRepository

class GetGenerate(
    private val context: Context,
    private val kmsapi: KMSAPI
) {
    fun generate(
        gemerateRequest: GemerateRequest,
        callback: (GenerateResponse?) -> Unit
    ){
        val kmsRepository = KmsRepository(kmsapi)

        kmsRepository.generate(gemerateRequest) { response, error ->
            if (response != null) {
                callback(response)
            } else {
                error?.printStackTrace()
            }
        }
    }
}