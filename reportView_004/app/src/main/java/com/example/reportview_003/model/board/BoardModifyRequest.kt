package com.example.reportview_003.model.board

data class BoardModifyRequest(
    val board_id: Int,
    val writer: String,
    val title: String,
    val content: String
)
