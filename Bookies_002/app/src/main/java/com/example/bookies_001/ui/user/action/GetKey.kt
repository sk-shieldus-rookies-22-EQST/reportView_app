package com.example.bookies_001.ui.user.action

import com.example.bookies_001.api.KMSAPI
import com.example.bookies_001.model.kms.GetkeyResponse
import com.example.bookies_001.repository.KmsRepository

class GetKey(
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