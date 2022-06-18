package com.mevron.rides.driver.trips.ui.state

import com.mevron.rides.driver.trips.domain.model.GetTripDomainData

data class GetTripState(
    val startDate: String,
    val endDate: String,
    val data: GetTripDomainData,
    val isLoading: Boolean,
    val isRequestSuccess: Boolean,
    val error: String
)
