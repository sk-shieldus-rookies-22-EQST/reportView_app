package com.example.reportview_003.model.purchase

data class CartResponse(
    val book_list : MutableList<MutableMap<String, Any>>,
    val cart_id : Int
)
