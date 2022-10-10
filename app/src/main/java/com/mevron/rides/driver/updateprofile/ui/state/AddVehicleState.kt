package com.mevron.rides.driver.updateprofile.ui.state

import com.mevron.rides.driver.updateprofile.domain.model.CarMake
import com.mevron.rides.driver.updateprofile.domain.model.CarModel
import com.mevron.rides.driver.updateprofile.domain.model.CarYearData

data class AddVehicleState(
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
    val isFromSideBar: Boolean
) {

    val isBottomSheetOpened: Boolean
        get() = isMakeBottomSheetOpen || isModelBottomSheetOpen || isYearBottomSheetOpen || isColorBottomSheetOpen

    companion object {

        val DEFAULT = AddVehicleState(
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
            isFromSideBar = false
        )
    }
}

sealed interface AddVehicleError {
    data class CarMakesRequestFailed(val message: String) : AddVehicleError
    data class CarYearRequestFailed(val message: String) : AddVehicleError
    data class CarModelsRequestFailed(val message: String) : AddVehicleError
    data class AddVehicleRequestFailed(val message: String) : AddVehicleError
    object None : AddVehicleError
}

data class CarMakeState(
    val carMake: String,
    val carMakeFilter: String,
    val isMakeListVisible: Boolean,
    val carMakeList: List<CarMake>
) {

    val currentMakes: List<CarMake>
        get() = carMakeList.filter { it.make.contains(carMakeFilter.trim(), ignoreCase = true) }

    val isEmpty: Boolean
        get() = carMakeList.isEmpty()

    companion object {

        val DEFAULT = CarMakeState(
            carMake = "",
            carMakeFilter = "",
            isMakeListVisible = false,
            carMakeList = listOf()
        )
    }
}

data class CarModelState(
    val carModel: String,
    val carModelFilter: String,
    val isCarModelVisible: Boolean,
    val carModelList: List<CarModel>
) {

    val isEmpty: Boolean
        get() = carModelList.isEmpty()

    val currentModels: List<CarModel>
        get() = carModelList.filter { it.model.contains(carModelFilter.trim(), ignoreCase = true) }

    companion object {

        val DEFAULT = CarModelState(
            carModel = "",
            carModelFilter = "",
            isCarModelVisible = false,
            carModelList = listOf()
        )
    }
}

data class CarColorState(
    val carColor: String,
    val isCarColorVisible: Boolean
) {
    companion object {

        val DEFAULT = CarColorState(carColor = "", isCarColorVisible = false)
    }
}

data class CarYearState(
    val carYear: String,
    val carYearFilter: String,
    val isCarYearVisible: Boolean,
    val carYears: List<CarYearData>
) {
    val currentYears: List<CarYearData>
        get() = this.carYears.filter { it.year.contains(carYearFilter.trim(), ignoreCase = true) }

    val isEmpty: Boolean
        get() = carYears.isEmpty()

    companion object {

        val DEFAULT = CarYearState(
            carYear = "",
            carYearFilter = "",
            isCarYearVisible = false,
            carYears = listOf()
        )
    }
}