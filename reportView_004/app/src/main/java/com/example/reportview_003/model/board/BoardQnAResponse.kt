package com.example.reportview_003.model.board

data class BoardQnAResponse(
    val id: Int,
    val user_id: String,
    val date: String,
    val title:String,
    val content:String ,
    val comment:String
)
