package com.project.kaamwaala.model.customer.cart

data class AddCartRequest(
    val Action: String,
    val CustomerId: String,
    val PriceItemId: String,
    val ProductId: String,
    val Quantity: String,
    val StoreId: String
)