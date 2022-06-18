package com.mevron.rides.driver.cashout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.cashout.data.model.AddBankAccountSpecification
import com.mevron.rides.driver.cashout.data.model.BankInfo
import com.mevron.rides.driver.cashout.data.model.BankRawInfo
import com.mevron.rides.driver.cashout.domain.model.AddAccount
import com.mevron.rides.driver.cashout.domain.model.AddAccountDomainData
import com.mevron.rides.driver.cashout.domain.model.PaymentDomainData
import com.mevron.rides.driver.cashout.domain.usecase.AddBankAccountUseCase
import com.mevron.rides.driver.cashout.domain.usecase.GetBankSpecificationUseCase
import com.mevron.rides.driver.cashout.ui.event.AddAccountEvent
import com.mevron.rides.driver.cashout.ui.state.AddAccountState
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddAccountViewModel @Inject constructor(
    private val useCase: AddBankAccountUseCase, private val getUseCase: GetBankSpecificationUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<AddAccountState> =
        MutableStateFlow(AddAccountState.EMPTY)

    val state: StateFlow<AddAccountState>
        get() = mutableState

    private fun updateAccount() {
        updateState(loading = true)
        val request = mutableState.value.toRequest()

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase(request)) {
                is DomainModel.Success -> {
                    val data = result.data as PaymentDomainData
                    updateState(
                        loading = false,
                        postSuccess = true
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        loading = false,
                        postSuccess = false,
                        errorMessage = "Failure to update your account details"
                    )
                }
            }
        }
    }

    private fun getAccountSpecif() {
        updateState(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getUseCase()) {
                is DomainModel.Success -> {
                    val data = result.data as AddAccountDomainData
                    updateState(
                        loading = false,
                        getSuccess = true,
                        setUpData = data.param
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        loading = false,
                        getSuccess = false,
                        errorMessage = "Failure to get requires account details"
                    )
                }
            }
        }
    }

    fun onEvent(event: AddAccountEvent) {
        when (event) {
            is AddAccountEvent.OnContinueClick -> {
                updateAccount()
            }
            AddAccountEvent.OnGetSpecicif -> getAccountSpecif()
        }
    }

    private fun AddAccountState.toRequest(): AddBankAccountSpecification =
        AddBankAccountSpecification(
            status = this.isDefault,
            bankInfo = this.data
        )

    fun updateState(
        loading: Boolean? = null,
        getSuccess: Boolean? = null,
        postSuccess: Boolean? = null,
        errorMessage: String? = null,
        updateRecord: Boolean? = null,
        isDefault: Int? = null,
        data: BankRawInfo? = null,
        setUpData: List<AddAccount>? = null
    ) {
        val currentState = mutableState.value
        var theData: MutableList<BankInfo> = arrayListOf()
        data?.let {
            if (currentState.data.isEmpty()) {
                theData.add(BankInfo(name = it.name, value = it.value))
            } else {
                theData = currentState.data.toMutableList()
                var found = false
                var theIndex = -1
                for ((index, value) in theData.withIndex()) {
                    if (value.name == data.name) {
                        found = true
                        theIndex = index
                        break
                    }
                }
                if (found) {
                    theData.set(theIndex, BankInfo(name = it.name, value = it.value))
                } else {
                    theData.add(BankInfo(name = it.name, value = it.value))
                }

            }
        } ?: run {
            theData = currentState.data.toMutableList()
        }
        mutableState.update {
            currentState.copy(
                loading = loading ?: currentState.loading,
                getSuccess = getSuccess ?: currentState.getSuccess,
                errorMessage = errorMessage ?: currentState.errorMessage,
                postSuccess = postSuccess ?: currentState.postSuccess,
                updateRecord = updateRecord ?: currentState.updateRecord,
                isDefault = isDefault ?: currentState.isDefault,
                data = theData,
                setUpData = setUpData ?: currentState.setUpData,
            )
        }
    }
}