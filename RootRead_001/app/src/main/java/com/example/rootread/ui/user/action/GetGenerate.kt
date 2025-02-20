//package com.example.bookies_001.ui.user.action
//
//import android.content.Context
//import com.example.bookies_001.api.KMSAPI
//import com.example.bookies_001.model.kms.GemerateRequest
//import com.example.bookies_001.model.kms.GenerateResponse
//import com.example.bookies_001.repository.KmsRepository
//
//class GetGenerate(
//    private val kmsapi: KMSAPI
//) {
//    fun generate(
//        gemerateRequest: GemerateRequest,
//        callback: (GenerateResponse?) -> Unit
//    ){
//        val kmsRepository = KmsRepository(kmsapi)
//
//        kmsRepository.generate(gemerateRequest) { response, error ->
//            if (response != null) {
//                callback(response)
//            } else {
//                error?.printStackTrace()
//            }
//        }
//    }
//}