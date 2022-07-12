package com.mevron.rides.driver.sidemenu.vehicle.ui.state

import com.mevron.rides.driver.sidemenu.vehicle.data.model.vehicledetail.VehicleDetailData
import com.mevron.rides.driver.sidemenu.vehicle.domain.model.AllVehicleDomainDatum

data class VehicleState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val error: String,
    val detail: VehicleDetailData,
    val vehicle: MutableList<AllVehicleDomainDatum>,
    val uuid: String,
    val backButton: Boolean
) {
    companion object {
        val EMPTY = VehicleState(
            isLoading = false,
            isSuccess = false,
            error = "",
            backButton = false,
            uuid = "",
            vehicle = mutableListOf(),
            detail = VehicleDetailData("", mutableListOf(), 0, "", "", "", "", "")
        )
    }
}