package com.project.kaamwaala.model.customer.cart

data class GetAddressRequest(
    val Action: String,
    val Address: String,
    val AddressId: String,
    val CustomerId: String
)