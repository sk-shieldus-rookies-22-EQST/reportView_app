package com.example.reportview_003.model.user

data class UserupdateRequest(
    val user_id: String,
    val user_pw: String,
    val user_phone: String,
    val user_email: String
)
