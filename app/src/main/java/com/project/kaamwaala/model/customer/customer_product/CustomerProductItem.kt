package com.project.kaamwaala.model.customer.customer_product

data class CustomerProductItem(
    val Description: String,
    val Id: String,
    val ImagePath: String,
    val Name: String,
    val ProductPriceItem: List<ProductPriceItem>
)