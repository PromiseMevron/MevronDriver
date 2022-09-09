package com.mevron.rides.driver.sidemenu.vehicle.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.sidemenu.vehicle.ui.state.AddNewVehicleState
import com.mevron.rides.driver.updateprofile.domain.model.AddVehicleRequest
import com.mevron.rides.driver.updateprofile.domain.model.CarMakesDomainData
import com.mevron.rides.driver.updateprofile.domain.model.CarModelDomainData
import com.mevron.rides.driver.updateprofile.domain.model.CarYearDomainData
import com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile.AddVehicleUseCaseAggregate
import com.mevron.rides.driver.updateprofile.ui.event.AddVehicleEvent
import com.mevron.rides.driver.updateprofile.ui.state.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddNewVehicleViewModel @Inject constructor(
    private val addVehicleUseCaseAggregate: AddVehicleUseCaseAggregate
) : ViewModel() {

    private val mutableState: MutableStateFlow<AddNewVehicleState> =
        MutableStateFlow(AddNewVehicleState.DEFAULT)

    val state: StateFlow<AddNewVehicleState>
        get() = mutableState

    private fun getYears() {
        clearError()
        val currentState = mutableState.value
        if (!currentState.carYearState.isEmpty) {
            updateState(
                isMakeBottomSheetOpen = false,
                isModelBottomSheetOpen = false,
                isYearBottomSheetOpen = true,
                isColorBottomSheetOpen = false,
                isLoading = false
            )
        } else {
            val param = currentState.carModelState.carModel
            if (param.isEmpty()) {
                updateState(error = AddVehicleError.CarYearRequestFailed("Please select a car model"))
            } else {
                fetchYearFromApi(param)
            }
        }
    }

    private fun fetchYearFromApi(param: String) {
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val request = paramToHashMap(param)

            val result = addVehicleUseCaseAggregate.getCarYearsUseCase(request)

            if (result is DomainModel.Success) {
                val data = result.data as CarYearDomainData
                val currentState = mutableState.value.carYearState
                updateState(
                    carYearState = currentState.copy(
                        carYears = data.carYearDataList
                    ),
                    isMakeBottomSheetOpen = false,
                    isModelBottomSheetOpen = false,
                    isYearBottomSheetOpen = true,
                    isColorBottomSheetOpen = false,
                    isLoading = false
                )
            } else {
                val message = (result as DomainModel.Error).error.localizedMessage
                    ?: ("Car Year Request Failed")
                updateState(
                    error = AddVehicleError.CarYearRequestFailed(message),
                    isLoading = false
                )
            }
        }
    }

    private fun getModels() {
        clearError()
        val currentState = mutableState.value
        if (!currentState.carModelState.isEmpty) {
            updateState(
                isMakeBottomSheetOpen = false,
                isModelBottomSheetOpen = true,
                isYearBottomSheetOpen = false,
                isColorBottomSheetOpen = false,
                isLoading = false
            )
        } else {
            val param = currentState.carMakeState.carMake
            if (param.isEmpty()) {
                updateState(error = AddVehicleError.CarModelsRequestFailed(message = "Please select a car make"))
            } else {
                fetchModelsFromApi(param)
            }
        }
    }

    private fun fetchModelsFromApi(param: String) {
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val request = paramToHashMap(param)

            val result = addVehicleUseCaseAggregate.getCarModelsUseCase(request)

            if (result is DomainModel.Success) {
                val data = result.data as CarModelDomainData
                val currentState = mutableState.value.carModelState
                updateState(
                    carModelState = currentState.copy(
                        carModelList = data.carModels
                    ),
                    isMakeBottomSheetOpen = false,
                    isModelBottomSheetOpen = true,
                    isYearBottomSheetOpen = false,
                    isColorBottomSheetOpen = false,
                    isLoading = false
                )
            } else {
                val message = (result as DomainModel.Error).error.localizedMessage
                    ?: ("Car Model Request Failed")
                updateState(
                    error = AddVehicleError.CarModelsRequestFailed(message),
                    isLoading = false
                )
            }
        }
    }

    private fun getCarMakes() {
        clearError()
        val currentState = mutableState.value
        if (!currentState.carMakeState.isEmpty) {
            updateState(
                isMakeBottomSheetOpen = true,
                isModelBottomSheetOpen = false,
                isYearBottomSheetOpen = false,
                isColorBottomSheetOpen = false,
                isLoading = false
            )
        } else {
            fetchCarMakesFromApi()
        }
    }

    private fun fetchCarMakesFromApi() {
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = addVehicleUseCaseAggregate.getCarMakesUseCase()

            if (result is DomainModel.Success) {
                val data = result.data as CarMakesDomainData
                val currentState = mutableState.value.carMakeState
                updateState(
                    carMakeState = currentState.copy(carMakeList = data.makes),
                    isMakeBottomSheetOpen = true,
                    isModelBottomSheetOpen = false,
                    isYearBottomSheetOpen = false,
                    isColorBottomSheetOpen = false,
                    isLoading = false
                )
            } else {
                val message = (result as DomainModel.Error).error.localizedMessage
                    ?: ("Car Make Request Failed")
                updateState(
                    error = AddVehicleError.CarMakesRequestFailed(message),
                    isLoading = false
                )
            }
        }
    }

    private fun clearError() {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(error = AddVehicleError.None)
        }
    }

    private fun submitData() {
        val request = mutableState.value.buildRequest()
        updateState(isSubmittingData = true)
        viewModelScope.launch(Dispatchers.IO) {
            val useCase = addVehicleUseCaseAggregate.addVehicleUseCase
            when (val result = useCase(request)) {
                is DomainModel.Success -> {
                    val id = result.data as String
                    updateState(isDataSubmitted = true, vehicleID = id)
                }
                is DomainModel.Error -> {
                    updateState(error = AddVehicleError.AddVehicleRequestFailed("Data submission failed"))
                }
            }
        }
    }

    fun onEventReceived(addVehicleEvent: AddVehicleEvent) {
        when (addVehicleEvent) {
            AddVehicleEvent.GetCarMakesClicked -> getCarMakes()
            AddVehicleEvent.GetCarModelsClicked -> getModels()
            AddVehicleEvent.GetCarYearsClicked -> getYears()
            AddVehicleEvent.GetCarColorsClicked -> updateState(isYearBottomSheetOpen = true)
            AddVehicleEvent.BackClicked -> handleBackClicked()
            AddVehicleEvent.AddVehicleClicked -> submitData()
            AddVehicleEvent.None -> {}
        }
    }

    private fun handleBackClicked() {
        val currentState = mutableState.value
        if (currentState.isBottomSheetOpened) {
            updateState(
                isYearBottomSheetOpen = false,
                isModelBottomSheetOpen = false,
                isMakeBottomSheetOpen = false,
                isColorBottomSheetOpen = false
            )
        } else {
            updateState(backClicked = true)
        }
    }

    private fun AddNewVehicleState.buildRequest() = AddVehicleRequest(
        color = carColorState.carColor,
        make = carMakeState.carMake,
        model = carModelState.carModel,
        plateNumber = platNumber,
        year = carYearState.carYear
    )

    fun updateSelectedData(
        selectedCarMake: String? = null,
        selectedCarModel: String? = null,
        selectedCarYear: String? = null,
        selectedCarColor: String? = null,
    ) {
        val currentState = mutableState.value
        val currentCarMakeState = mutableState.value.carMakeState
        val currentCarModelState = mutableState.value.carModelState
        val currentCarYearState = mutableState.value.carYearState
        val currentCarColorState = mutableState.value.carColorState
        mutableState.update {
            currentState.copy(
                carMakeState = currentState.carMakeState.copy(
                    carMake = selectedCarMake.takeIfNull(currentCarMakeState.carMake)
                ),
                carModelState = currentState.carModelState.copy(
                    carModel = updateSelectedList(
                        selectedCarMake,
                        currentCarMakeState,
                        selectedCarModel,
                        currentCarModelState
                    ),
                    carModelList = updateSelectedModelList(
                        selectedCarMake,
                        currentCarMakeState,
                        currentCarModelState
                    )
                ),
                carYearState = currentState.carYearState.copy(
                    carYear = updateSelectedYear(
                        selectedCarMake,
                        currentCarMakeState,
                        selectedCarModel,
                        currentCarModelState,
                        selectedCarYear,
                        currentCarYearState
                    ),
                    carYears = updateSelectedYears(
                        selectedCarMake,
                        currentCarMakeState,
                        selectedCarModel,
                        currentCarModelState,
                        currentCarYearState
                    )
                ),
                carColorState = currentState.carColorState.copy(
                    carColor = selectedCarColor.takeIfNull(currentCarColorState.carColor)
                )
            )
        }
    }

    private fun updateSelectedList(
        selectedCarMake: String?,
        currentCarMakeState: CarMakeState,
        selectedCarModel: String?,
        currentCarModelState: CarModelState
    ) = if (selectedCarMake != null && selectedCarMake != currentCarMakeState.carMake) {
        ""
    } else {
        selectedCarModel.takeIfNull(currentCarModelState.carModel)
    }

    private fun updateSelectedModelList(
        selectedCarMake: String?,
        currentCarMakeState: CarMakeState,
        currentCarModelState: CarModelState
    ) = if (selectedCarMake != null && selectedCarMake == currentCarMakeState.carMake) {
        listOf()
    } else {
        currentCarModelState.carModelList
    }

    private fun updateSelectedYear(
        selectedCarMake: String?,
        currentCarMakeState: CarMakeState,
        selectedCarModel: String?,
        currentCarModelState: CarModelState,
        selectedCarYear: String?,
        currentCarYearState: CarYearState
    ) = if (selectedCarMake != null &&
        selectedCarMake != currentCarMakeState.carMake ||
        selectedCarModel != null && selectedCarModel != currentCarModelState.carModel
    ) {
        ""
    } else {
        selectedCarYear.takeIfNull(currentCarYearState.carYear)
    }

    private fun updateSelectedYears(
        selectedCarMake: String?,
        currentCarMakeState: CarMakeState,
        selectedCarModel: String?,
        currentCarModelState: CarModelState,
        currentCarYearState: CarYearState
    ) = if (selectedCarMake != null &&
        selectedCarMake != currentCarMakeState.carMake ||
        selectedCarModel != null && selectedCarModel != currentCarModelState.carModel
    ) {
        listOf()
    } else {
        currentCarYearState.carYears
    }

    fun filterMake(carMakeFilter: String? = null) {
        carMakeFilter?.let { filter ->
            val currentState = mutableState.value
            val currentMake = mutableState.value.carMakeState
            mutableState.update {
                currentState.copy(
                    carMakeState = currentMake.copy(
                        carMakeFilter = filter
                    )
                )
            }
        }
    }

    fun filterYear(
        carYearFilter: String? = null,
    ) {
        carYearFilter?.let { filter ->
            val currentState = mutableState.value
            val currentYear = mutableState.value.carYearState
            mutableState.update {
                currentState.copy(
                    carYearState = currentYear.copy(
                        carYearFilter = filter
                    )
                )
            }
        }
    }

    fun filterModel(
        carModelFilter: String? = null
    ) {
        carModelFilter?.let { filter ->
            val currentState = mutableState.value
            val currentModel = mutableState.value.carModelState
            mutableState.update {
                currentState.copy(
                    carModelState = currentModel.copy(
                        carModelFilter = filter
                    )
                )
            }
        }
    }

    fun updateState(
        carYearState: CarYearState? = null,
        carMakeState: CarMakeState? = null,
        carModelState: CarModelState? = null,
        licenseNumber: String? = null,
        isColorBottomSheetOpen: Boolean? = null,
        isModelBottomSheetOpen: Boolean? = null,
        isYearBottomSheetOpen: Boolean? = null,
        isMakeBottomSheetOpen: Boolean? = null,
        error: AddVehicleError? = null,
        backClicked: Boolean? = null,
        isSubmittingData: Boolean? = null,
        isDataSubmitted: Boolean? = null,
        isLoading: Boolean? = null,
        isFromSideBar: Boolean? = null,
        vehicleID: String? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                carYearState = carYearState.takeIfNull(currentState.carYearState),
                carMakeState = carMakeState.takeIfNull(currentState.carMakeState),
                carModelState = carModelState.takeIfNull(currentState.carModelState),
                platNumber = licenseNumber.takeIfNull(currentState.platNumber),
                isColorBottomSheetOpen = isColorBottomSheetOpen.takeIfNull(currentState.isColorBottomSheetOpen),
                isModelBottomSheetOpen = isModelBottomSheetOpen.takeIfNull(currentState.isModelBottomSheetOpen),
                isYearBottomSheetOpen = isYearBottomSheetOpen.takeIfNull(currentState.isYearBottomSheetOpen),
                isMakeBottomSheetOpen = isMakeBottomSheetOpen.takeIfNull(currentState.isYearBottomSheetOpen),
                error = error.takeIfNull(currentState.error),
                backClicked = backClicked.takeIfNull(currentState.backClicked),
                isSubmittingData = isSubmittingData.takeIfNull(currentState.isSubmittingData),
                isDataSubmitted = isDataSubmitted.takeIfNull(currentState.isDataSubmitted),
                isLoading = isLoading.takeIfNull(currentState.isLoading),
                isFromSideBar = isFromSideBar.takeIfNull(currentState.isFromSideBar),
                vehicleId = vehicleID.takeIfNull(currentState.vehicleId)
            )
        }
    }

    private fun paramToHashMap(param: String): HashMap<String, String> =
        HashMap<String, String>().apply { this["type"] = param }
}

fun <T> T?.takeIfNull(other: T): T = this ?: other