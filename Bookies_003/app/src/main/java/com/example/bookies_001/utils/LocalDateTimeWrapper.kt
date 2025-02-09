package com.example.bookies_001.utils

import java.time.LocalDateTime

class LocalDateTimeWrapper(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int
) {
    // LocalDateTime을 LocalDateTimeWrapper로 변환
    companion object {
        fun fromLocalDateTime(dateTime: LocalDateTime): LocalDateTimeWrapper {
            return LocalDateTimeWrapper(
                year = dateTime.year,
                month = dateTime.monthValue,
                day = dateTime.dayOfMonth,
                hour = dateTime.hour,
                minute = dateTime.minute,
                second = dateTime.second
            )
        }

        // JSON 데이터를 다시 LocalDateTime 객체로 변환
        fun toLocalDateTime(wrapper: LocalDateTimeWrapper): LocalDateTime {
            return LocalDateTime.of(wrapper.year, wrapper.month, wrapper.day, wrapper.hour, wrapper.minute, wrapper.second)
        }
    }
}