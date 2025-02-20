package com.example.rootread.model.board

data class BoardModifyRequest(
    val qna_id: Long,
    val writer: String,
    val title: String,
    val content: String,
    val secret: Boolean
)
