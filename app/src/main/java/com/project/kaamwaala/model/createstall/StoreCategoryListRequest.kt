package com.project.kaamwaala.model.createstall

data class StoreCategoryListRequest(
    val Action: String,
    val CategoryName: String,
    val Code: String,
    val ImagePath: String,
    val StoreId: String,
    val PageNumber: String
)