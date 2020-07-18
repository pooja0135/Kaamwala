package com.project.kaamwaala.model.customer.dashboard

data class DashboardResponse(
    val BannerList: List<Banner>,
    val Message: String,
    val NewLaunchedList: List<NewLaunched>,
    val OfferList: List<Offer>,
    val Status: Boolean
)