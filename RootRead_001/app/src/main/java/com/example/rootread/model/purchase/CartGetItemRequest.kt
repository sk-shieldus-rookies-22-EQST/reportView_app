package com.example.rootread.model.purchase

data class CartGetItemRequest(
    val user_id: String,
    val book_id: Long
)
