package com.mevron.rides.driver.trips.data.model.alltrips

data class AllTripsData(
    val currency: String,
    val earning: Int,
    val endDate: String,
    val online: String,
    val results: List<AllTripsResult>,
    val rides: Int,
    val startDate: String
)