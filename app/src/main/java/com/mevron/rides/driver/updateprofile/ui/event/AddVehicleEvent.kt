package com.mevron.rides.driver.updateprofile.ui.event

sealed interface AddVehicleEvent {
    object BackClicked : AddVehicleEvent
    object GetCarMakesClicked: AddVehicleEvent
    object GetCarModelsClicked: AddVehicleEvent
    object GetCarYearsClicked: AddVehicleEvent
    object GetCarColorsClicked: AddVehicleEvent
    object AddVehicleClicked: AddVehicleEvent
    object None : AddVehicleEvent
}