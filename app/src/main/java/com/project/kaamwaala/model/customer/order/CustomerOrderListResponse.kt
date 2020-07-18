package com.project.kaamwaala.model.customer.order

data class CustomerOrderListResponse(
    val Message: String,
    val OrderList: List<Order>,
    val Status: Boolean
)