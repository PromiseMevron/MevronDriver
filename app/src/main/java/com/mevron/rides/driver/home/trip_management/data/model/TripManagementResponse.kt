package com.mevron.rides.driver.home.trip_management.data.model

data class TripManagementResponse(
    val success: TripManagementSuccess
)

data class TripManagementSuccess(
    val message: String,
    val status: String
)
