package com.mevron.rides.driver.trips.domain.model

import com.mevron.rides.driver.home.data.model.home.Trip
import com.mevron.rides.driver.trips.data.model.alltrips.AllTripsResult

data class GetTripDomainData(
    val amount: String = "",
    val endDate: String = "",
    val online: String = "",
    val results: List<AllTripsResult> = mutableListOf(),
    val rides: Int = 0,
    val startDate: String = ""
)
