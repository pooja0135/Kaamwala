package com.project.kaamwaala.model.customer.cart

data class AddAddressRequest(
    val Action: String,
    val Address: String,
    val AddressId: String,
    val CustomerId: String
)