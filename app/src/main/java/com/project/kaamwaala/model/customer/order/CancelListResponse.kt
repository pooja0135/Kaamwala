package com.project.kaamwaala.model.customer.order

data class CancelListResponse(
    val CancelReasonList: List<CancelReason>,
    val Message: String,
    val Status: Boolean
)