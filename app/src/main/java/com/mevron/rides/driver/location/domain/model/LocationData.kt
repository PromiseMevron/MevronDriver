package com.mevron.rides.driver.location.domain.model

data class LocationData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isForeground: Boolean = true,
    val bearing: Float = 0.0F
)