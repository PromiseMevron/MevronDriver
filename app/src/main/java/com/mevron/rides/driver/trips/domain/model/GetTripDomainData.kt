package com.mevron.rides.driver.trips.domain.model

data class GetTripDomainData(
    val amount: String = "",
    val endDate: String = "",
    val online: String = "",
    val results: List<AllTripsDomainData> = mutableListOf(),
    val rides: Int = 0,
    val startDate: String = ""
)
