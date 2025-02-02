package com.example.reportview_003.model.view

data class ViewbookdetailResponse(
    val book_id: Long,
    val book_summary: String,
    val writer: String,
    val title: String,
    val price:String,
    val book_img_path: String
)
