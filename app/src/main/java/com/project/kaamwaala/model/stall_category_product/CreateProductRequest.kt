package com.project.kaamwaala.model.stall_category_product

data class CreateProductRequest(
    val Action: String,
    val Code: String,
    val Description: String,
    val Name: String,
    val StoreCategoryId: String,
    val StoreId: String
)