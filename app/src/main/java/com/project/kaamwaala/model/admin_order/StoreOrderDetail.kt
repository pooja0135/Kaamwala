package com.project.kaamwaala.model.admin_order

data class StoreOrderDetail(
    val Description: String,
    val ImagePath: String,
    val ItemId: String,
    val ProductId: String,
    val ProductName: String,
    val Quantity: String,
    val TotalPrice: String,
    val UnitPrice: String
)