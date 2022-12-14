package com.mevron.rides.driver.sidemenu.savedplaces.ui.addaddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.sidemenu.savedplaces.domain.model.GetAddressDomainData
import com.mevron.rides.driver.sidemenu.savedplaces.domain.model.GetSavedAddressData
import com.mevron.rides.driver.sidemenu.savedplaces.domain.usecases.GetAddressUseCase
import com.mevron.rides.driver.sidemenu.savedplaces.ui.addaddress.event.AddSavedAddressEvent
import com.mevron.rides.driver.sidemenu.savedplaces.ui.addaddress.state.AddSavedPlaceState
import com.mevron.rides.driver.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSavedPlaceViewModel @Inject constructor(private val useCase: GetAddressUseCase) :
    ViewModel() {

    private val mutableState: MutableStateFlow<AddSavedPlaceState> =
        MutableStateFlow(AddSavedPlaceState.EMPTY)

    val state: StateFlow<AddSavedPlaceState>
        get() = mutableState

    private fun getAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase()) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = result.error.localizedMessage ?: Constants.UNEXPECTED_ERROR
                    )
                }
                is DomainModel.Success -> {
                    val data = result.data as GetAddressDomainData
                    updateState(
                        isLoading = false,
                        isRequestSuccess = true,
                        savedAddresses = data.savedAddresses.toMutableList()
                    )
                }
            }
        }
    }

    fun handleEvent(event: AddSavedAddressEvent) {
        when (event) {
            is AddSavedAddressEvent.OnAddNewClicked ->
                updateState(updateAddress = true)
            is AddSavedAddressEvent.GetNewAddress -> getAddress()
            is AddSavedAddressEvent.OnBackButtonClicked -> updateState(backButton = true)
        }
    }

    fun updateState(
        isLoading: Boolean? = null,
        isRequestSuccess: Boolean? = null,
        savedAddresses: MutableList<GetSavedAddressData>? = null,
        updateAddress: Boolean? = null,
        backButton: Boolean? = null,
        error: String? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                isLoading = isLoading ?: currentState.isLoading,
                isSuccess = isRequestSuccess ?: currentState.isSuccess,
                data = savedAddresses ?: currentState.data,
                openNextPage = updateAddress ?: currentState.openNextPage,
                backButton = backButton ?: currentState.backButton,
                error = error ?: currentState.error
            )
        }
    }
}