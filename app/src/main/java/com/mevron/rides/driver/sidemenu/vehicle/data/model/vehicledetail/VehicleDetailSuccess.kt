package com.mevron.rides.driver.sidemenu.vehicle.data.model.vehicledetail

import com.google.gson.annotations.SerializedName

data class VehicleDetailSuccess(
    @SerializedName("data")
    val vehicleDetailData: VehicleDetailData,
    val message: String,
    val status: String
)