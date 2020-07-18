package com.project.kaamwaala.model.customer.customer_store

data class CustomerStoreResponse(
    val BannerList: List<Banner>,
    val Message: String,
    val Status: Boolean,
    val StoreList: List<Store>
)