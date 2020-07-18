package com.project.kaamwaala.model.admin_stall_detail

data class ShippingChargesRequest(
    val Action: String,
    val Code: String,
    val store_id: String,
    val svalue: String
)