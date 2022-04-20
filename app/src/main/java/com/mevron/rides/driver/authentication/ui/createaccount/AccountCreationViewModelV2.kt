package com.mevron.rides.driver.authentication.ui.createaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.usecase.CreateAccountUseCase
import com.mevron.rides.driver.authentication.ui.createaccount.event.CreateAccountEvent
import com.mevron.rides.driver.authentication.ui.createaccount.state.CreateAccountState
import com.mevron.rides.driver.domain.DomainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountCreationViewModelV2(private val useCase: CreateAccountUseCase) : ViewModel() {

    private val mutableState: MutableStateFlow<CreateAccountState> =
        MutableStateFlow(CreateAccountState.EMPTY)

    val sate: StateFlow<CreateAccountState>
        get() = mutableState

    fun handleEvent(event: CreateAccountEvent) {

    }

    private fun createAccount(request: CreateAccountRequest) {
        viewModelScope.launch {
            val result = useCase(request)
            if (result is DomainModel.Success) {

            }
        }
    }
}