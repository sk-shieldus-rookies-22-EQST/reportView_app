package com.example.rootread.model.purchase

data class CartResponse(
    val purchaseCartDtoList : MutableList<MutableMap<String, Any>>,
)
