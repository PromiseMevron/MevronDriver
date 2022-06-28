package com.mevron.rides.driver.home.data.model.sockets

import com.google.gson.annotations.SerializedName

data class RideRequestSocketData(
    @SerializedName("current_state")
    val currentState: String,
    @SerializedName("meta_data")
    val rideRequestMetaData: RideRequestMetaData
)