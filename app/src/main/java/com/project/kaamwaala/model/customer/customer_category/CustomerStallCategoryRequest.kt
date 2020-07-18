package com.project.kaamwaala.model.customer.customer_category

data class CustomerStallCategoryRequest(
    val Action: String,
    val CityId: String,
    val MainCategoryId: String,
    val StoreCategoryId: String,
    val StoreId: String
)