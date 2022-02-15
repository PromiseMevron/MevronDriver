package com.mevron.rides.driver.auth.model

data class VehicleAddRequest(
    val color: String,
    val make: String,
    val model: String,
    val plateNumber: String,
    val preference: String = "1",
    val year: String
)