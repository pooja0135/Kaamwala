package com.project.kaamwaala.model.stall_category_product

data class StoreProductListResponse(
    val Message: String,
    val ProductItem: List<ProductItem>,
    val Status: Boolean
)