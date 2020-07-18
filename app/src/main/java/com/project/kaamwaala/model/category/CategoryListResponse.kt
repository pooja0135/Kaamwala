package com.project.kaamwaala.model.category

data class CategoryListResponse(
    val CategoryListItem: List<CategoryItem>,
    val Message: String,
    val Status: Boolean
)