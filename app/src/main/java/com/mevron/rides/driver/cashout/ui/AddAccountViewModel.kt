package com.mevron.rides.driver.cashout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.cashout.data.model.AddAccountPayload
import com.mevron.rides.driver.cashout.data.model.AddBankAccountSpecification
import com.mevron.rides.driver.cashout.data.model.BankInfo
import com.mevron.rides.driver.cashout.data.model.BankRawInfo
import com.mevron.rides.driver.cashout.domain.model.*
import com.mevron.rides.driver.cashout.domain.usecase.*
import com.mevron.rides.driver.cashout.ui.event.AddAccountEvent
import com.mevron.rides.driver.cashout.ui.state.AddAccountState
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val useCase: AddBankAccountUseCase, private val getUseCase: GetBankSpecificationUseCase,
    private val bankListUseCase: GetBankListUseCase,  private val resolveUseCase: ResolveAcountNumberUseCase, private val addBankUseCase: AddBankUseCase
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

    fun getBankList() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = bankListUseCase()) {
                is DomainModel.Success -> {
                    val data = result.data as BankListDomainData
                    updateState(
                        loading = false,
                        bankList = data.bankData
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

    fun resolveAccount() {
        val accountNumber = mutableState.value.accountNumber
        val bankCode = mutableState.value.bankCode
        val bankName = mutableState.value.bankName
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = resolveUseCase(AddAccountPayload(account_number = accountNumber, account_bank = bankCode))) {
                is DomainModel.Success -> {
                    val data = result.data as ResolveBankDomainData
                    updateState(
                        loading = false,
                        customerName = data.account_name
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

    fun addNewAccount() {
        val accountNumber = mutableState.value.accountNumber
        val bankCode = mutableState.value.bankCode
        val bankName = mutableState.value.bankName
        val accountName = mutableState.value.customerName
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = addBankUseCase(AddAccountPayload(account_number = accountNumber, bank_code = bankCode, account_name = accountName, bank_name = bankName))) {
                is DomainModel.Success -> {
                    updateState(
                        loading = false,
                        postSuccess = true
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
        setUpData: List<AddAccount>? = null,
        customerName: String? = null,
        accountNumber: String? = null,
        bankName: String? = null,
        bankCode: String? = null,
        bankList: List<BankList>? = null
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
                customerName = customerName ?: currentState.customerName,
                accountNumber = accountNumber ?: currentState.accountNumber,
                bankCode = bankCode ?: currentState.bankCode,
                bankName = bankName ?: currentState.bankName,
                bankList = bankList ?: currentState.bankList
            )
        }
    }
}