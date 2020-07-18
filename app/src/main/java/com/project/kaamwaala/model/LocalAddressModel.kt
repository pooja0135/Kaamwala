package com.project.kaamwaala.model

import java.io.Serializable

data class LocalAddressModel(
    var houseBuildingName: String,
    var areaColony: String,
    var city: String,
    var state: String,
    var pin: String,
    var landmark: String

): Serializable