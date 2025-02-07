package com.example.bookies_001.model.purchase

data class CartGetItemRequest(
    val user_id: String,
    val book_id: Long
)
