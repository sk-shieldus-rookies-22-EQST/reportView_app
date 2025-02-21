package com.example.rootread.model.board

import java.time.LocalDateTime

data class EachQnA(
    val qna_id:Long ,
    val title: String ,
    val user_id: String,
    val secret: Boolean,
    val created_at: LocalDateTime
)
