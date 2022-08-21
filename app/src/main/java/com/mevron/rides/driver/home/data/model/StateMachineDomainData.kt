package com.mevron.rides.driver.home.data.model

import com.google.gson.annotations.SerializedName

data class StateMachineResponse(
    @SerializedName("current_state")
    val currentState: String,
    @SerializedName("meta_data")
    val metaData: MetaData?
)

data class MetaData(
    @SerializedName("trip_id")
    val tripId: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("pickupLatitude")
    val pickupLatitude: String?,
    @SerializedName("pickupLongitude")
    val pickupLongitude: String?,
    @SerializedName("destinationLatitude")
    val destinationLatitude: String?,
    @SerializedName("destinationLongitude")
    val destinationLongitude: String?,
    @SerializedName("estimatedDistance")
    val estimatedDistance: Any?,
    @SerializedName("estimatedTripTime")
    val estimatedTripTime: String?,
    @SerializedName("riderRating")
    val riderRating: String?,
    @SerializedName("riderImage")
    val riderImage: String?,
    @SerializedName("riderName")
    val riderName: String?,
    @SerializedName("destinationAddress")
    val destinationAddress: String?,
    @SerializedName("pickupAddress")
    val pickupAddress: String?,
    @SerializedName("amount")
    val amount: String?,
    @SerializedName("currency")
    val currency: String?
)