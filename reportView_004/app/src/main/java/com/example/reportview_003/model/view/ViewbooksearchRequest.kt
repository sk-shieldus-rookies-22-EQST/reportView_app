package com.example.reportview_003.model.view

import java.time.LocalDateTime

data class ViewbooksearchRequest(
    val keyword: String? = null,
    val sdate: LocalDateTime? = null,
    val edate: LocalDateTime? = null,
)
