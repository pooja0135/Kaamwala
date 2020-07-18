package com.project.kaamwaala.model.adminstalllist

data class AdminStallListResponse(
    val Message: String,
    val Status: Boolean,
    val StoreListItem: List<StoreItem>
)