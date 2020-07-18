package com.project.kaamwaala.model.customer.cart

data class GetCartResponse(
    val About: String,
    val Address: String,
    val AllowAdvanceBooking: String,
    val CartItem: List<CartItem>,
    val ClosingTime: String,
    val DeleveryBoundry: String,
    val EnableCOD: String,
    val EnableOnlinePayment: String,
    val EnableOrderByList: String,
    val EnablePaymentOnDelevery: String,
    val EnablePickup: String,
    val EnableRating: String,
    val EnableStanderdDelivery: String,
    val FreeShopingAmt: String,
    val GSTRate: String,
    val IsChat: String,
    val IsVacation: String,
    val Latitude: String,
    val ListName: String,
    val Location: String,
    val Longitude: String,
    val Message: String,
    val MinAdvanceBokingDays: String,
    val MinDeleveryTime: String,
    val MinOrderValue: String,
    val OpeningTime: String,
    val OwnerImageUrl: String,
    val OwnerName: String,
    val PhoneNumber: String,
    val Rating: String,
    val RefundPolicy: String,
    val ShopingCharges: String,
    val StanderdDeliveryBoundry: String,
    val Status: Boolean,
    val StoreImageUrl: String,
    val StoreName: String,
    val Store_Id: String,
    val Tagline: String,
    val TermsAndCondition: String,
    val VacationMessage: String,
    val Votes: String,
    val WeeklyOff: String
)