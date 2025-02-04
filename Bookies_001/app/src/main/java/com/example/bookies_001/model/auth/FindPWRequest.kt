package com.example.bookies_001.model.auth

data class FindPWRequest(
    val user_id: String,
    val user_pw: String,
)
