package com.example.reportview_003.model.board

data class BoardWriteRequest(
    val title: String,
    val content: String,
    val userID: String
)
