package com.project.kaamwaala.model.customer.order

data class Order(
    val Address: String,
    val CancelReason: String,
    val DeliveryStatus: String,
    val DeliveryTime: String,
    val DiscountAmount: String,
    val GstAmount: String,
    val Name: String,
    val Note: String,
    val OrderDate: String,
    val OrderId: String,
    val PackagingAmount: String,
    val PaymentMode: String,
    val PaymentStatus: String,
    val PhoneNo: String,
    val ShippingCharge: String,
    val StoreName: String,
    val TagLine: String,
    val TotalAmount: String
)

