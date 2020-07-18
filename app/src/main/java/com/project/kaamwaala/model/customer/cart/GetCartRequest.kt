package com.project.kaamwaala.model.customer.cart

data class GetCartRequest(
    val Action: String,
    val CityId: String,
    val MainCategoryId: String,
    val StoreCategoryId: String,
    val CustomerId: String,
    val StoreId: String

)