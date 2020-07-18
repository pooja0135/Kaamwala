package com.project.kaamwaala.model.customer.order

data class PlaceOrderResponse(
    val Message: String,
    val MobileNo: String,
    val Name: String,
    val NoOfItem: String,
    val OrderId: String,
    val Status: Boolean
)