package com.example.rootread.model.view

import java.time.LocalDateTime

data class EachBook(
    val book_id : Long,
    val title : String,
    val price : Int,
    val writer : String,
    val write_date : LocalDateTime,
    val book_img_path : String
)
