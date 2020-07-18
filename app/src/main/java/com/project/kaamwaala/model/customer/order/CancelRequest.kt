package com.project.kaamwaala.model.customer.order

data class CancelRequest(
    val CancelReason: String,
    val OrderId: String
)