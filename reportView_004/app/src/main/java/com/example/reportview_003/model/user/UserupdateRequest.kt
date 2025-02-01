package com.example.reportview_003.model.user

data class UserupdateRequest(
    val user_id: String,
    val user_pw: String? = null,
    val user_phone: String? = null,
    val user_email: String? = null
)
