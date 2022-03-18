package com.mevron.rides.driver.service

data class UpdateLocationModel(
    val lat: String,
    val long: String,
    val uuid: String? = null,
    val trip_id: String? = null
)