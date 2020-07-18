package com.project.kaamwaala.model.customer.cart

data class CartItem(
    val CustomerId: String,
    val Describe: String,
    val ImagePath: String,
    val Price: String,
    val PriceItemId: String,
    val ProductDescription: String,
    val ProductId: String,
    val ProductName: String,
    val Quantity: String,
    val SalePrice: String,
    val StoreId: String
)