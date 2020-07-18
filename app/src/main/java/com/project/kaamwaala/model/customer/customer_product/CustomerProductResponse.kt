package com.project.kaamwaala.model.customer.customer_product

data class CustomerProductResponse(
    val CustomerProductItem: List<CustomerProductItem>,
    val Message: String,
    val Status: Boolean
)