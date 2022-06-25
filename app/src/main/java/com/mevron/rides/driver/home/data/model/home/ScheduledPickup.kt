package com.mevron.rides.driver.home.data.model.home

data class ScheduledPickup(
    val destinationAddress: String,
    val destinationLatitude: String,
    val destinationLongitude: String,
    val id: String,
    val maxEstimatedCost: String,
    val minEstimatedCost: String,
    val pickupAddress: String,
    val pickupLatitude: String,
    val pickupLongitude: String,
    val scheduleDeliveryTime: String,
    val schedulePickupTime: String
)