package com.example.rootread.model.board

data class BoardQnAResponse(
    val qna_id: Long,
    val writer: String,
    val created_at: String,
    val title:String,
    val content:String,
    val comment:MutableList<MutableMap<String,Any>>,
    val file_name:String,
    val file_path:String,
    val secret:Boolean
)
