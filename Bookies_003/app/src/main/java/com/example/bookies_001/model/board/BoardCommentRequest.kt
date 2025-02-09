package com.example.bookies_001.model.board


data class BoardCommentRequest(
    val qna_id: Long,
    val writer: String,
    val content: String
)
