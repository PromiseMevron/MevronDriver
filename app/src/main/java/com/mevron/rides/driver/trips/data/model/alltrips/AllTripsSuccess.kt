package com.mevron.rides.driver.trips.data.model.alltrips

import com.google.gson.annotations.SerializedName

data class AllTripsSuccess(
    @SerializedName("data")
    val allTripsData: AllTripsData,
    val message: String,
    val status: String
)