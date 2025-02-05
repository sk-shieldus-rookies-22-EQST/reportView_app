package com.example.bookies_001.model.view

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ViewbooksearchRequest(
    val keyword: String,
    val sdate: String?,
    val edate: String?
) {
    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")

        fun fromLocalDateTime(
            keyword: String,
            sdate: LocalDateTime?,
            edate: LocalDateTime?
        ): ViewbooksearchRequest {
            return ViewbooksearchRequest(
                keyword = keyword,
                sdate = sdate?.format(formatter),  // LocalDateTime → String 변환
                edate = edate?.format(formatter)   // LocalDateTime → String 변환
            )
        }
    }
}

