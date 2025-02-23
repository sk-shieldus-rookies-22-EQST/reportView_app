package com.example.bookies_001.model.view

import java.time.LocalDateTime

data class EachBook(
    val book_id : Long = 0L,
    val title : String = "Non Title",
    val price : Int = 0,
    val writer : String = "Non Writer",
    val write_date : String = "Non Date",
    val book_img_path : String ?= null,
)
