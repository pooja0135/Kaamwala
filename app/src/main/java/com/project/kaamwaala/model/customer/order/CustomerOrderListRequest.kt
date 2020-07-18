package com.project.kaamwaala.model.customer.order

data class CustomerOrderListRequest(
    val Action: String,
    val CustomerId: String,
    val OrderId: String
)