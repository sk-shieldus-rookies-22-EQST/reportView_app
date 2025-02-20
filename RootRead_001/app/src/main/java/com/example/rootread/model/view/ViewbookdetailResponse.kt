package com.example.rootread.model.view

data class ViewbookdetailResponse(
    val book_id: Long,
    val book_summary: String,
    val writer: String,
    val title: String,
    val price:String,
    val write_date: String,
    val book_img_path: String
)
