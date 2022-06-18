package com.mevron.rides.driver.remote

data class TripManagementModel(
    val code: String? = null,
    val trip_id: String,
    val type: String,
    val rating: Int? = null,
    val amount: String? = null
)