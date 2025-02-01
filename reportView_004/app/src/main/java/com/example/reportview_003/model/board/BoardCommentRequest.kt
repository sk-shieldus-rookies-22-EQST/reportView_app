package com.example.reportview_003.model.board


data class BoardCommentRequest(
    val qna_id: Int,
    val writer: String,
    val content: String
)
