package com.project.kaamwaala.model.customer.order

data class PlaceOrderRequest(
    val Action: String,
    val CustomerAddressId: String,
    val CustomerId: String,
    val DeliveryTime: String,
    val DiscountAmount: String,
    val GSTAmount: String,
    val GrandTotal: String,
    val PackagingAmount: String,
    val PaymentMode: String,
    val ShippingAmount: String,
    val StoreId: String,
    val TotalAmount: String,
    val note: String,
    val paymentstatus: String,
    val phoneno: String
)