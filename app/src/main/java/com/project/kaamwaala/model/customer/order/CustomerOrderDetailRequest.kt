package com.project.kaamwaala.model.customer.order

data class CustomerOrderDetailRequest(
    val Action: String,
    val CustomerId: String,
    val OrderId: String
)