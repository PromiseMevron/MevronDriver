package com.mevron.rides.driver.home.data.model.sockets

data class RideRequestMetaData(
    val destinationAddress: String,
    val destinationLatitude: String,
    val destinationLongitude: String,
    val estimatedDistance: String,
    val estimatedPickupTime: String,
    val estimatedTripTime: String,
    val pickupAddress: String,
    val pickupLatitude: String,
    val pickupLongitude: String,
    val riderImage: String,
    val riderName: String,
    val riderRating: String,
    val status: String,
    val tripId: String
)