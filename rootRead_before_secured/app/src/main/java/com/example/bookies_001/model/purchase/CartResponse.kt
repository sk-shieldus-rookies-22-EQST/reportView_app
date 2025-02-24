package com.example.bookies_001.model.purchase

data class CartResponse(
    val purchaseCartDtoList : MutableList<MutableMap<String, Any>>,
)
