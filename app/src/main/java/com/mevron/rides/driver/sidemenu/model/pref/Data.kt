package com.mevron.rides.driver.sidemenu.model.pref

data class Data(
    val acceptCash: Int,
    val acceptTrips: Int,
    val excludeLowRated: Int,
    val longDistance: Int,
    val preferredNavigation: String
)