package com.project.kaamwaala.model.admin_stall_detail

data class MinOrderRequest(
    val Action: String,
    val Code: String,
    val store_id: String,
    val svalue: String
)