package com.example.reportview_003.model.purchase

data class CartResponse(
    val book_list : MutableList<MutableMap<String, Any>>,
    val total_price : Int?=null,
    val cart_id : Int
)
