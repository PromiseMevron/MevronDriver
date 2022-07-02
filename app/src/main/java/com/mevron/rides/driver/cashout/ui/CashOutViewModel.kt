package com.mevron.rides.driver.cashout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.cashout.data.model.CashActionData
import com.mevron.rides.driver.cashout.domain.model.PaymentDetailsDomainData
import com.mevron.rides.driver.cashout.domain.usecase.CashOutUseCase
import com.mevron.rides.driver.cashout.domain.usecase.GetWalletDetailsUseCase
import com.mevron.rides.driver.cashout.ui.state.GetWalletDetailState
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CashOutViewModel @Inject constructor(
    private val useCase: GetWalletDetailsUseCase,
    private val cashOutUseCase: CashOutUseCase
) :
    ViewModel() {

    private val mutableState: MutableStateFlow<GetWalletDetailState> =
        MutableStateFlow(GetWalletDetailState.EMPTY)

    val state: StateFlow<GetWalletDetailState>
        get() = mutableState

    fun getWalletDetails() {
        updateState(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase()) {
                is DomainModel.Success -> {
                    val data = result.data as PaymentDetailsDomainData
                    updateState(
                        loading = false,
                        errorMessage = "",
                        balance = data.balance
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        loading = false,
                        errorMessage = "Failure to get your wallet details",
                        balance = ""
                    )
                }
            }
        }
    }

    fun cashOutWallet() {
        updateState(loading = true)
        val data = mutableState.value.toRequest()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = cashOutUseCase(data = data)) {
                is DomainModel.Success -> {
                    updateState(
                        loading = false,
                        errorMessage = "",
                        success = true
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        loading = false,
                        errorMessage = "Failure to cashing out",
                    )
                }
            }
        }
    }


    private fun GetWalletDetailState.toRequest(): CashActionData =
        CashActionData(
            amount = this.balance
        )


    fun updateState(
        loading: Boolean? = null,
        errorMessage: String? = null,
        balance: String? = null,
        success: Boolean? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                loading = loading ?: currentState.loading,
                errorMessage = errorMessage ?: currentState.errorMessage,
                balance = balance ?: currentState.balance,
                success = success ?: currentState.success
            )
        }
    }
}