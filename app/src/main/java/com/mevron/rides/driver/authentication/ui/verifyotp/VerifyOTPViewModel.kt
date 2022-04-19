package com.mevron.rides.driver.authentication.ui.verifyotp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.authentication.domain.usecase.VerifyOTPUseCase
import com.mevron.rides.driver.authentication.ui.verifyotp.event.VerifyOTPEvent
import com.mevron.rides.driver.authentication.ui.verifyotp.state.VerifyOTPState
import com.mevron.rides.driver.domain.DomainModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VerifyOTPViewModel @Inject constructor(
    private val verifyOTPUseCase: VerifyOTPUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<VerifyOTPState> =
        MutableStateFlow(VerifyOTPState.EMPTY)

    val state: StateFlow<VerifyOTPState>
        get() = mutableState

    private fun verifyOTP() {
        val request = mutableState.value.toRequest()
        viewModelScope.launch {
            val result = verifyOTPUseCase(request)
            if (result is DomainModel.Success) {
                // update state with request success
            } else {
                // update state with error occurred
            }
        }
    }

    fun onEvent(event: VerifyOTPEvent) {
        when (event) {
            is VerifyOTPEvent.OnOTPComplete -> {
                verifyOTP()
            }

            // on error or snack back clicked, update state by remove error and success
        }
    }

    private fun VerifyOTPState.toRequest(): VerifyOTPRequest =
        VerifyOTPRequest(
            code = this.code,
            phoneNumber = this.phoneNumber
        )
}