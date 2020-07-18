package com.project.kaamwaala.model.stallcategory

data class CreateCategoryRequest(
    val Action: String,
    val CategoryName: String,
    val Code: String,
    val ImagePath: String,
    val StoreId: String,
    val PageNumber: String


)