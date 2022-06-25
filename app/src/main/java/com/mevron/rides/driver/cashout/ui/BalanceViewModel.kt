package com.mevron.rides.driver.cashout.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.cashout.data.model.CashOutData
import com.mevron.rides.driver.cashout.domain.model.PaymentBalanceDetailsDomainDatum
import com.mevron.rides.driver.cashout.domain.model.PaymentDetailsDomainData
import com.mevron.rides.driver.cashout.domain.model.PaymentDetailsDomainDatum
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
class BalanceViewModel @Inject constructor(private val useCase: GetWalletDetailsUseCase, private val cashOutUseCase: CashOutUseCase) :
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
                    createSection(data)
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
        val balance = mutableState.value.balance
        if (balance.isEmpty()){
            return
        }
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


    private fun GetWalletDetailState.toRequest(): CashOutData =
        CashOutData(
            amount = this.balance
        )

    private fun createSection(data: PaymentDetailsDomainData){
        val arr = data.data.sortedByDescending {
            it.date.replace("-", "")
        }
        val arr1 = mutableListOf<PaymentBalanceDetailsDomainDatum>()
        var arr2 = mutableListOf<PaymentDetailsDomainDatum>()
        var headingDate = ""

        for (i in arr.indices){
            if (i == 0){
                arr[i].apply {
                    headingDate = this.date.toReadableDate()
                   arr2.add(PaymentDetailsDomainDatum(amount, date, icon, method, narration, time))
                }
            }else{
                if (arr[i - 1].date == arr[i].date){
                    arr[i].apply {
                        headingDate = this.date.toReadableDate()
                        arr2.add(PaymentDetailsDomainDatum(amount, date, icon, method, narration, time))
                    }
                }else{
                    arr1.add(PaymentBalanceDetailsDomainDatum(date = headingDate, data = arr2))
                    arr2 = mutableListOf()
                    arr[i].apply {
                        headingDate = this.date.toReadableDate()
                        arr2.add(PaymentDetailsDomainDatum(amount, date, icon, method, narration, time))
                    }
                }
            }
            if (i == arr.size - 1) {
                arr1.add(PaymentBalanceDetailsDomainDatum(date = headingDate, data = arr2))
            }
        }
        updateState(data = arr1)
    }

    private fun String.toReadableDate() : String{
        val theDate = this.split("-")
        return "${theDate[1].toMonth()} ${theDate[2]}, ${theDate[0]}"
    }

    private fun String.toMonth() : String{
        if (this == "01"){
            return "January"
        }
        if (this == "02"){
            return "February"
        }
        if (this == "03"){
            return "March"
        }
        if (this == "04"){
            return "April"
        }
        if (this == "05"){
            return "May"
        }
        if (this == "06"){
            return "June"
        }
        if (this == "07"){
            return "July"
        }
        if (this == "08"){
            return "August"
        }
        if (this == "09"){
            return "September"
        }
        if (this == "10"){
            return "October"
        }
        if (this == "11"){
            return "November"
        }
        if (this == "12"){
            return "December"
        }
        return ""
    }

    fun updateState(
        loading: Boolean? = null,
        errorMessage: String? = null,
        balance: String? = null,
        success: Boolean? = null,
        data: List<PaymentBalanceDetailsDomainDatum>? = null,
        date: String? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                loading = loading ?: currentState.loading,
                errorMessage = errorMessage ?: currentState.errorMessage,
                balance = balance ?: currentState.balance,
                success = success ?: currentState.success,
                data = data ?: currentState.data,
                date = date ?: currentState.date
            )
        }
    }
}