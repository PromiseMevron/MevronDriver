package com.mevron.rides.driver.home.data.model.home

import com.google.gson.annotations.SerializedName

data class WeeklySummary(
    val earnings: String,
    val online: String,
    val rides: Int,
    @SerializedName("trip_list")
    val tripList: List<TripX>
)