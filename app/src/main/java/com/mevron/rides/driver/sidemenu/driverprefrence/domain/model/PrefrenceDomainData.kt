package com.mevron.rides.driver.sidemenu.driverprefrence.domain.model

data class PrefrenceDomainData(
    val acceptCash: Int = 0,
    val acceptTrips: Int = 0,
    val excludeLowRated: Int = 0,
    val longDistance: Int = 0,
    val preferredNavigation: String = "maps"
)
