package com.mevron.rides.driver.location.domain.model

data class LocationData(
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val isForeground: Boolean = true,
    val direction: Float = 0.0F,
    val uuid: String = ""
)