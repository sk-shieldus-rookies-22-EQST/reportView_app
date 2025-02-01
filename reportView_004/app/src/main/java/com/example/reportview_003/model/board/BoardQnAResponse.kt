package com.example.reportview_003.model.board

import java.time.LocalDateTime

data class BoardQnAResponse(
    val board_id: Int,
    val writer: String,
    val created_at: String,
    val title:String,
    val content:String,
    val comment:MutableList<MutableMap<String,Any>>,
    val file_name:String,
    val file_path:String
)
