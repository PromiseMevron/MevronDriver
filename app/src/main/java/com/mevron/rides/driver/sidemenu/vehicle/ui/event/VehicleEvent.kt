package com.mevron.rides.driver.sidemenu.vehicle.ui.event

sealed interface VehicleEvent {
    object MakeAPICall: VehicleEvent
    object MakeAPICallDetails: VehicleEvent
    object DeleteVehicle: VehicleEvent
}