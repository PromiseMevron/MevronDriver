package com.mevron.rides.driver.updateprofile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile.UploadSecurityNumberUseCase
import com.mevron.rides.driver.updateprofile.ui.event.AddSocialSecurityNumberError
import com.mevron.rides.driver.updateprofile.ui.event.AddSocialSecurityNumberEvent
import com.mevron.rides.driver.updateprofile.ui.event.AddSocialSecurityNumberState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SocialSecurityViewModel @Inject constructor(val useCase: UploadSecurityNumberUseCase) : ViewModel() {

    private val mutableState: MutableStateFlow<AddSocialSecurityNumberState> =
        MutableStateFlow(AddSocialSecurityNumberState.DEFAULT)

    val state: StateFlow<AddSocialSecurityNumberState>
        get() = mutableState

    private fun uploadSocialSecurityNumber() {
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val request = mutableState.value.buildRequest()
            when (val response = useCase(request)) {
                is DomainModel.Success -> updateState(isLoading = false, isRequestSuccess = true)
                is DomainModel.Error -> updateState(error = response.error.message, isLoading = false)
            }
        }
    }

    fun onEventReceived(event: AddSocialSecurityNumberEvent) {
        when (event) {
            AddSocialSecurityNumberEvent.BackButtonClick -> updateState(shouldRouteBack = true)
            AddSocialSecurityNumberEvent.AddSocialSecurityNumberButtonClick -> uploadSocialSecurityNumber()
        }
    }

    fun updateState(
        isLoading: Boolean? = null,
        socialSecurityNumber: String? = null,
        shouldRouteBack: Boolean? = null,
        error: String? = null,
        isRequestSuccess: Boolean? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                isLoading = isLoading ?: currentState.isLoading,
                socialSecurityNumber = socialSecurityNumber ?: currentState.socialSecurityNumber,
                shouldRouteBack = shouldRouteBack ?: currentState.shouldRouteBack,
                error = error?.let { AddSocialSecurityNumberError.RequestError(error) } ?: currentState.error,
                isRequestSuccess = isRequestSuccess ?: currentState.isRequestSuccess
            )
        }
    }
}