package com.mevron.rides.driver.updateprofile.domain.model

data class AddVehicleRequest(
    val color: String,
    val make: String,
    val model: String,
    val plateNumber: String,
    val preference: String = "1",
    val year: String
)