package com.example.rootread.model.auth

data class FindPWRequest(
    val user_id: String,
    val new_user_pw: String,
)
