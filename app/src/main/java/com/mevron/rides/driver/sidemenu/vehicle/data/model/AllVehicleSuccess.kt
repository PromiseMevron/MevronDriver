package com.mevron.rides.driver.sidemenu.vehicle.data.model

import com.google.gson.annotations.SerializedName

data class AllVehicleSuccess(
    @SerializedName("data")
    val allVehicleData: List<AllVehicleData>,
    val message: String,
    val status: String
)