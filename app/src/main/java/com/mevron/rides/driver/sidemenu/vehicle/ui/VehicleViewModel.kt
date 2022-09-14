package com.mevron.rides.driver.sidemenu.vehicle.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.sidemenu.vehicle.data.model.vehicledetail.VehicleDetailData
import com.mevron.rides.driver.sidemenu.vehicle.domain.model.AllVehicleDomainData
import com.mevron.rides.driver.sidemenu.vehicle.domain.model.AllVehicleDomainDatum
import com.mevron.rides.driver.sidemenu.vehicle.domain.usecase.DeleteVehicleUseCase
import com.mevron.rides.driver.sidemenu.vehicle.domain.usecase.GetVehicleDetailUseCase
import com.mevron.rides.driver.sidemenu.vehicle.domain.usecase.GetVehiclesUseCase
import com.mevron.rides.driver.sidemenu.vehicle.domain.usecase.UpdateVehicleUseCase
import com.mevron.rides.driver.sidemenu.vehicle.ui.event.VehicleEvent
import com.mevron.rides.driver.sidemenu.vehicle.ui.state.VehicleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val useCase: GetVehiclesUseCase,
    private val detailUseCase: GetVehicleDetailUseCase,
    private val deleteVehicleUseCase: DeleteVehicleUseCase,
    private val updateVehicleUseCase: UpdateVehicleUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<VehicleState> =
        MutableStateFlow(VehicleState.EMPTY)

    val state: StateFlow<VehicleState>
        get() = mutableState

    fun onEvent(event: VehicleEvent) {
        when (event) {
            VehicleEvent.DeleteVehicle -> deleteAVehiclesDetails()
            VehicleEvent.MakeAPICall -> getAllVehicles()
            VehicleEvent.MakeAPICallDetails -> getAVehiclesDetails()
        }
    }

    private fun getAllVehicles() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase()) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = "failure in getting vehicles",
                        peakHeight = 0
                    )
                }
                is DomainModel.Success -> {
                    val data = result.data as AllVehicleDomainData
                    val defaultCar = data.data.find {
                        it.preference
                    }?.make
                    val peakHeight = if (data.data.isEmpty()){
                        330
                    }else if (data.data.size == 1){
                        650
                    }else{
                        900
                    }

                    updateState(
                        isLoading = false,
                        vehicle = data.data.toMutableList(),
                        peakHeight = peakHeight,
                        default = defaultCar
                    )
                }
            }
        }
    }

    private fun getAVehiclesDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val id = mutableState.value.uuid
            when (val result = detailUseCase(id)) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = "failure in getting vehicle details"
                    )
                }
                is DomainModel.Success -> {
                    val data = result.data as VehicleDetailData
                    updateState(
                        isLoading = false,
                        detail = data
                    )
                }
            }
        }
    }

    private fun deleteAVehiclesDetails() {
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val id = mutableState.value.uuid
            when (deleteVehicleUseCase(id)) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = "failure to delete vehicle"
                    )
                }
                is DomainModel.Success -> {
                    updateState(
                        isLoading = false,
                        isRequestSuccess = true
                    )
                }
            }
        }
    }

     fun updateAVehicle() {
        viewModelScope.launch(Dispatchers.IO) {
            val id = mutableState.value.uuid
            when (updateVehicleUseCase(id)) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                    )
                }
                is DomainModel.Success -> {
                    updateState(

                    )
                }
            }
        }
    }

    fun updateState(
        isLoading: Boolean? = null,
        isRequestSuccess: Boolean? = null,
        vehicle: MutableList<AllVehicleDomainDatum>? = null,
        backButton: Boolean? = null,
        error: String? = null,
        detail: VehicleDetailData? = null,
        uuid: String? = null,
        peakHeight: Int? = null,
        default: String? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                isLoading = isLoading ?: currentState.isLoading,
                isSuccess = isRequestSuccess ?: currentState.isSuccess,
                vehicle = vehicle ?: currentState.vehicle,
                backButton = backButton ?: currentState.backButton,
                error = error ?: currentState.error,
                detail = detail ?: currentState.detail, uuid = uuid ?: currentState.uuid,
                peakHeight = peakHeight ?: currentState.peakHeight,
                defaultCar = default ?: currentState.defaultCar
            )
        }
    }
}