package com.mevron.rides.driver.sidemenu.vehicle.ui.state

import com.mevron.rides.driver.updateprofile.ui.state.*


data class AddNewVehicleState(
    val platNumber: String,
    val carYearState: CarYearState,
    val carModelState: CarModelState,
    val carColorState: CarColorState,
    val carMakeState: CarMakeState,
    val error: AddVehicleError,
    val isColorBottomSheetOpen: Boolean,
    val isYearBottomSheetOpen: Boolean,
    val isModelBottomSheetOpen: Boolean,
    val isMakeBottomSheetOpen: Boolean,
    val backClicked: Boolean,
    val isSubmittingData: Boolean,
    val isDataSubmitted: Boolean,
    val isLoading: Boolean,
    val isFromSideBar: Boolean,
    val vehicleId: String
) {

    val isBottomSheetOpened: Boolean
        get() = isMakeBottomSheetOpen || isModelBottomSheetOpen || isYearBottomSheetOpen || isColorBottomSheetOpen

    companion object {

        val DEFAULT = AddNewVehicleState(
            platNumber = "",
            carYearState = CarYearState.DEFAULT,
            carColorState = CarColorState.DEFAULT,
            carModelState = CarModelState.DEFAULT,
            carMakeState = CarMakeState.DEFAULT,
            error = AddVehicleError.None,
            isColorBottomSheetOpen = false,
            isYearBottomSheetOpen = false,
            isModelBottomSheetOpen = false,
            isMakeBottomSheetOpen = false,
            backClicked = false,
            isSubmittingData = false,
            isDataSubmitted = false,
            isLoading = false,
            isFromSideBar = false,
            vehicleId = ""
        )
    }
}