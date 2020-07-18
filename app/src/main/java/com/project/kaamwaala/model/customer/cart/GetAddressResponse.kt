package com.project.kaamwaala.model.customer.cart

data class GetAddressResponse(
    val CustomerAddresList: List<CustomerAddres>,
    val Message: String,
    val Status: Boolean
)