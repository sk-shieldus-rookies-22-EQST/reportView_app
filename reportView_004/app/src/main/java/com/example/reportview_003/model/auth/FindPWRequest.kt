package com.example.reportview_003.model.auth

data class FindPWRequest(
    val user_id: String,
    val phone: String,
    val email: String
)
