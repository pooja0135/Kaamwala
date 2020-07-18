package com.project.kaamwaala.model.admin_order

data class AdminOrderDetailResponse(
    val CancelReason: String,
    val CustomerAddress: String,
    val CustomerMobileNo: String,
    val DeliveryStatus: String,
    val DeliveryTime: String,
    val DiscountAmount: String,
    val GrandTotal: String,
    val GstAmount: String,
    val IsChat: String,
    val Message: String,
    val OrderDate: String,
    val PackagingAmount: String,
    val PaymentMode: String,
    val PaymentStatus: String,
    val ShippingCharge: String,
    val Status: Boolean,
    val StoreAddress: String,
    val StoreId: String,
    val StoreImageUrl: String,
    val StoreLocation: String,
    val StoreMobileNo: String,
    val StoreName: String,
    val StoreOrderDetailList: List<StoreOrderDetail>,
    val TagLine: String,
    val TotalAmount: String
)