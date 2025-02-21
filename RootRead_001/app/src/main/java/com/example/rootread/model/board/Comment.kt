package com.example.rootread.model.board

import java.time.LocalDateTime

data class Comment(
    val qna_re_id: Long,
    val qna_id: Long,
    val qna_re_content: String,
    val qna_re_created_at: LocalDateTime
)
