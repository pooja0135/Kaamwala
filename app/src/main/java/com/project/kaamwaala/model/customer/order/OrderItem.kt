package com.project.kaamwaala.model.customer.order

data class OrderItem(
    val Description: String,
    val ImagePath: String,
    val ItemId: String,
    val ProductId: String,
    val ProductName: String,
    val Quantity: String,
    val TotalPrice: String,
    val UnitPrice: String
)