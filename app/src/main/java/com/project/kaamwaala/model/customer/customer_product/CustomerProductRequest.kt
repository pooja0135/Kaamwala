package com.project.kaamwaala.model.customer.customer_product

data class CustomerProductRequest(
    val Action: String,
    val CityId: String,
    val MainCategoryId: String,
    val StoreCategoryId: String,
    val StoreId: String
)