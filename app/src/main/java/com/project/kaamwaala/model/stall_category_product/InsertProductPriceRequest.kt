package com.project.kaamwaala.model.stall_category_product

data class InsertProductPriceRequest(
    val Action: String,
    val Code: String,
    val Describe: String,
    val Price: String,
    val ProductId: String,
    val SalePrice: String,
    val IsStock: String
)