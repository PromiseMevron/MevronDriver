package com.mevron.rides.driver.sidemenu.savedplaces.ui.addressdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.remote.MevronRepo
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.SaveAddressRequest
import com.mevron.rides.driver.sidemenu.savedplaces.domain.usecases.SaveAddressUseCase
import com.mevron.rides.driver.sidemenu.savedplaces.ui.addressdetail.event.SaveAddressDetailsEvent
import com.mevron.rides.driver.sidemenu.savedplaces.ui.addressdetail.state.SaveAddressDetailState
import com.mevron.rides.driver.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SaveAddressDetailsViewModel @Inject constructor(private val useCase: SaveAddressUseCase) :
    ViewModel() {

    private val mutableState: MutableStateFlow<SaveAddressDetailState> =
        MutableStateFlow(SaveAddressDetailState.EMPTY)
    val state: StateFlow<SaveAddressDetailState>
        get() = mutableState

    private fun saveAddress() {
        updateState(isLoading = true)
        val data = mutableState.value.buildRequest()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase(data)) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = result.error.localizedMessage ?: Constants.UNEXPECTED_ERROR
                    )
                }
                is DomainModel.Success -> {
                    updateState(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            }
        }
    }

    private fun SaveAddressDetailState.buildRequest(): SaveAddressRequest =
        SaveAddressRequest(
            address = address,
            latitude = lat.toString(),
            longitude = lng.toString(),
            type = type,
            name = type

        )

    fun handleEvent(event: SaveAddressDetailsEvent) {
        when (event) {
            SaveAddressDetailsEvent.BackButtonPressed -> updateState(backButton = true)
            SaveAddressDetailsEvent.SaveAddressClicked -> saveAddress()
        }
    }

    fun updateState(
        isLoading: Boolean? = null,
        isSuccess: Boolean? = null,
        backButton: Boolean? = null,
        error: String? = null,
        lat: Double? = null,
        lng: Double? = null,
        address: String? = null,
        type: String? = null,
        name: String? = null
    ) {
        val currentState = mutableState.value
        val isCorrect = (name ?: currentState.name).isNotEmpty() && (address
            ?: currentState.address).isNotEmpty()
        mutableState.update {
            currentState.copy(
                isLoading = isLoading ?: currentState.isLoading,
                isSuccess = isSuccess ?: currentState.isSuccess,
                backButton = backButton ?: currentState.backButton,
                type = type ?: currentState.type,
                name = name ?: currentState.name,
                lat = lat ?: currentState.lat,
                lng = lng ?: currentState.lng,
                address = address ?: currentState.address,
                error = error ?: currentState.error,
                isCorrect = isCorrect
            )
        }
    }
}