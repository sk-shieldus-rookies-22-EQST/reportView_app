package com.example.bookies_001.model.view

import com.example.bookies_001.utils.LocalDateTimeWrapper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ViewbooksearchRequest(
    val keyword: String,
    val sdate: LocalDateTimeWrapper?,
    val edate: LocalDateTimeWrapper?
) {
    companion object {
        fun fromLocalDateTime(
            keyword: String,
            sdate: LocalDateTime?,
            edate: LocalDateTime?
        ): ViewbooksearchRequest {
            return ViewbooksearchRequest(
                keyword = keyword,
                sdate = sdate?.let { LocalDateTimeWrapper.fromLocalDateTime(it) }, // 변환 적용
                edate = edate?.let { LocalDateTimeWrapper.fromLocalDateTime(it) }
            )
        }
    }
}

