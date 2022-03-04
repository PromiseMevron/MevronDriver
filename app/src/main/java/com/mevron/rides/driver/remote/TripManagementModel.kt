package com.mevron.rides.driver.remote

data class TripManagementModel(
    val code: String,
    val trip_id: String,
    val type: String? = null
)