package com.mevron.rides.driver.sidemenu.emerg.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.sidemenu.emerg.data.model.UpdateEmergencyContact
import com.mevron.rides.driver.sidemenu.emerg.domain.usecase.DeleteContactUseCase
import com.mevron.rides.driver.sidemenu.emerg.domain.usecase.UpdateContactUseCase
import com.mevron.rides.driver.sidemenu.emerg.ui.EmergencyEvent
import com.mevron.rides.driver.sidemenu.emerg.ui.EmergencyState
import com.mevron.rides.driver.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateEmergencyViewModel @Inject constructor(
    private val useCase: UpdateContactUseCase,
    private val deleteCase: DeleteContactUseCase
) :
    ViewModel() {

    private val mutableState: MutableStateFlow<EmergencyState> =
        MutableStateFlow(EmergencyState.EMPTY)

    val state: StateFlow<EmergencyState>
        get() = mutableState

    private fun deleteEmergency() {
        updateState(isLoading = true)
        val id = mutableState.value.contactId
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = deleteCase(id)) {
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
                        isRequestSuccess = true,
                    )
                }
            }
        }
    }

    private fun updateEmergency() {
        updateState(isLoading = true)
        val data = mutableState.value.buildRequest()
        val id = mutableState.value.contactId
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase(id, data)) {
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
                        isRequestSuccess = true,
                    )
                }
            }
        }
    }

    private fun EmergencyState.buildRequest(): UpdateEmergencyContact {
        return UpdateEmergencyContact(phoneNumber = phone, name = name, details = time)
    }

    fun handleEvent(event: EmergencyEvent) {
        when (event) {
            is EmergencyEvent.DeleteContact ->
                deleteEmergency()
            is EmergencyEvent.UpdateContact -> updateEmergency()
            else -> {}
        }
    }

    fun updateState(
        isLoading: Boolean? = null,
        isRequestSuccess: Boolean? = null,
        name: String? = null,
        phoneNumber: String? = null,
        id: String? = null,
        time: MutableList<Int>? = null,
        error: String? = null,
    ) {
        val currentState = mutableState.value


        mutableState.update {
            currentState.copy(
                isLoading = isLoading ?: currentState.isLoading,
                isSuccess = isRequestSuccess ?: currentState.isSuccess,
                name = name ?: currentState.name,
                phone = phoneNumber ?: currentState.phone,
                contactId = id ?: currentState.contactId,
                time = time ?: currentState.time,
                error = error ?: currentState.error
            )
        }
    }

}