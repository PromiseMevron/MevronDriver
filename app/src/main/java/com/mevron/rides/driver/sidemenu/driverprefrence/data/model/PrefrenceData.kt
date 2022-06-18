package com.mevron.rides.driver.sidemenu.driverprefrence.data.model

data class PrefrenceData(
    val acceptCash: Int,
    val acceptTrips: Int,
    val excludeLowRated: Int,
    val longDistance: Int,
    val preferredNavigation: String?
)