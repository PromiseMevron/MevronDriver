package com.mevron.rides.driver.home.data.model

import com.google.gson.annotations.SerializedName

data class StateMachineResponse(
    @SerializedName("current_state")
    val currentState: String,
    @SerializedName("meta_data")
    val metaData: MetaData
)

data class MetaData(
    @SerializedName("trip_id")
    val tripId: String?,
    @SerializedName("status")
    val status: String,
    @SerializedName("pickupLatitude")
    val pickupLatitude: String,
    @SerializedName("pickupLongitude")
    val pickupLongitude: String,
    @SerializedName("destinationLatitude")
    val destinationLatitude: String,
    @SerializedName("destinationLongitude")
    val destinationLongitude: String

)