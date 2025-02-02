package com.example.reportview_003.model.purchase

data class CartResponse(
    val purchaseCartDtoList : MutableList<MutableMap<String, Any>>,
)
