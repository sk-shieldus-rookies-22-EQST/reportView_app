package com.example.bookies_001.model.auth

data class FindIDRequest(
    val user_phone: String,
    val user_email: String
)
