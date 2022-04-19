package com.mevron.rides.driver.authentication.ui.registerphone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.authentication.domain.model.RegisterPhoneDomainData
import com.mevron.rides.driver.authentication.domain.model.RegisterPhoneRequest
import com.mevron.rides.driver.authentication.domain.usecase.RegisterPhoneUseCase
import com.mevron.rides.driver.authentication.ui.registerphone.event.RegisterPhoneEvent
import com.mevron.rides.driver.authentication.ui.registerphone.state.RegisterPhoneState
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterPhoneViewModel @Inject constructor(
    private val useCase: RegisterPhoneUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<RegisterPhoneState> =
        MutableStateFlow(RegisterPhoneState.EMPTY)

    val state: StateFlow<RegisterPhoneState>
        get() = mutableState

    private fun registerPhone(request: RegisterPhoneRequest) {
        updateState(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase(request)) {
                is DomainModel.Success -> {
                    val data = result.data as RegisterPhoneDomainData
                    updateState(
                        loading = false,
                        requestSuccess = true,
                        phoneCodeExpiration = data.phoneCodeData.expireAt,
                        code = data.phoneCodeData.code
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        loading = false,
                        requestSuccess = false,
                        error = result.toState()
                    )
                }
            }
        }
    }

    fun onEvent(event: RegisterPhoneEvent) {
        when (event) {
            RegisterPhoneEvent.NextButtonClick -> {
                registerPhone(mutableState.value.buildRequest())
            }
        }
    }

    private fun RegisterPhoneState.buildRequest(): RegisterPhoneRequest =
        RegisterPhoneRequest(
            country = country,
            phoneNumber = countryCodeAndPhoneNumber
        )

    private fun DomainModel.Error.toState(): String =
        this.error.localizedMessage ?: "Phone number registration failed"

    fun updateState(
        loading: Boolean? = false,
        country: String? = null,
        countryCode: String? = null,
        phoneNumber: String? = null,
        requestSuccess: Boolean? = false,
        error: String? = null,
        phoneCodeExpiration: String? = null,
        number: String? = null,
        isCorrectNumber: Boolean? = null,
        code: String? = null
    ) {
        val currentValue = mutableState.value
        mutableState.update {
            currentValue.copy(
                code = code ?: currentValue.code,
                isCorrectNumber = isCorrectNumber ?: currentValue.isCorrectNumber,
                loading = loading ?: currentValue.loading,
                country = country ?: currentValue.country,
                countryCode = countryCode ?: currentValue.countryCode,
                phoneNumber = phoneNumber ?: currentValue.phoneNumber,
                requestSuccess = requestSuccess ?: currentValue.requestSuccess,
                error = error ?: currentValue.error,
                phoneCodeExpiration = phoneCodeExpiration ?: currentValue.phoneCodeExpiration,
                number = number ?: currentValue.number,
                isValidNumber = number != null && number.length !in 11..14
            )
        }
    }
}
