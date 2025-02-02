package com.example.reportview_003.model.board

data class BoardModifyRequest(
    val qna_id: Long,
    val writer: String,
    val title: String,
    val content: String
)
