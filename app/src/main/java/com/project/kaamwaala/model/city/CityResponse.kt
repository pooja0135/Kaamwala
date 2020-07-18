package com.project.kaamwaala.model.city

data class CityResponse(
    val CityItem: List<CityItem>,
    val Message: String,
    val Status: Boolean
)