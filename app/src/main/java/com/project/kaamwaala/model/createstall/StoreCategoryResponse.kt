package com.project.kaamwaala.model.createstall

data class StoreCategoryResponse(
    val Message: String,
    val Status: Boolean,
    val StoreCategoryItem: List<StoreCategoryItem>
)