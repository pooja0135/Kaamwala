package com.project.kaamwaala.model.admin_order

data class AdminOrderDetailRequest(
    val Action: String,
    val AdminId: String,
    val OrderDate: String,
    val OrderId: String
)