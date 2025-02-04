package com.example.bookies_001.model.kms

data class GenerateResponse(
    val message: String?,
    val presigned_url: String?,
    val error: String?
)
