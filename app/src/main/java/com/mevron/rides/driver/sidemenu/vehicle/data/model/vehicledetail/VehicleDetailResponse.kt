package com.mevron.rides.driver.sidemenu.vehicle.data.model.vehicledetail

import com.google.gson.annotations.SerializedName

data class VehicleDetailResponse(
    @SerializedName("success")
    val vehicleDetailSuccess: VehicleDetailSuccess
)