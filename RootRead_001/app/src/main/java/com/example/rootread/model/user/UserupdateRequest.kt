package com.example.rootread.model.user

data class UserupdateRequest(
    val user_id: String?,
    val user_pw: String?,
    val user_phone: String?,
    val user_email: String?
)
