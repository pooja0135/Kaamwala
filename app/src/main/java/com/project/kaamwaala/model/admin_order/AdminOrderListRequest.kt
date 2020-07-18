package com.project.kaamwaala.model.admin_order

data class AdminOrderListRequest(
    val Action: String,
    val AdminId: String,
    val OrderDate: String,
    val OrderId: String
)