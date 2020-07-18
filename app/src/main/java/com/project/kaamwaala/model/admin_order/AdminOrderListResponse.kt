package com.project.kaamwaala.model.admin_order

data class AdminOrderListResponse(
    val Message: String,
    val Status: Boolean,
    val StoreOrderList: List<StoreOrder>
)