package com.example.reportview_003.model.view

data class ViewbooksearchRequest(
    val keyword: String? = null,
    val sdate: String?=null,
    val edate: String?=null,
    val theme: String?=null
)
