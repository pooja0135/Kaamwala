package com.project.kaamwaala.model.stall_category_product

data class StoreProductDetailResponse(
    val Code: String,
    val Description: String,
    val Message: String,
    val Name: String,
    val ProductImagesItem: List<ProductImagesItem>,
    val ProductPriceItem: List<ProductPriceItem>,
    val Status: Boolean
)