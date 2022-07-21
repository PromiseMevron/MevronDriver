package com.mevron.rides.driver.sidemenu.vehicle.data.model

import com.google.gson.annotations.SerializedName

data class AllVehiclesResponse(
    @SerializedName("success")
    val allVehicle: AllVehicleSuccess
)