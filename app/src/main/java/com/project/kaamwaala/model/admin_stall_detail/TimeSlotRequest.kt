package com.project.kaamwaala.model.admin_stall_detail

data class TimeSlotRequest(
    val Action: String,
    val Code: String,
    val store_id: String,
    val svalue: String
)