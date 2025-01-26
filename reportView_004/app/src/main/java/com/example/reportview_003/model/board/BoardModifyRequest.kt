package com.example.reportview_003.model.board

data class BoardModifyRequest(
    val board_id: Int,
    val user_id: String,
    val title: String,
    val content: String
)
