package com.project.kaamwaala.model.stall_category_product

data class InsertProductImageRequest(
    val Action: String,
    val Code: String,
    val ImagePath: String,
    val ProductId: String,
    val StoreImageUrl: String,
    val WebUrl: String
)