package com.project.kaamwaala.model.stall_category_product

data class StoreProductListRequest(
    val Action: String,
    val Code: String,
    val Description: String,
    val Name: String,
    val StoreCategoryId: String,
    val StoreId: String,
    val PageNumber: String
)