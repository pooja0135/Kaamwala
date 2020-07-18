package com.project.kaamwaala.model.customer.cart

data class UpdateProfileRequest(
    val Action: String,
    val CustomerId: String,
    val PhoneNo: String
)