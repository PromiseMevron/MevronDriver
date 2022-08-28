package com.mevron.rides.driver.cashout.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.cashout.data.model.CashActionData
import com.mevron.rides.driver.cashout.data.model.GetLinkAmount
import com.mevron.rides.driver.cashout.domain.model.*
import com.mevron.rides.driver.cashout.domain.usecase.*
import com.mevron.rides.driver.cashout.ui.event.CashOutAddFundEvent
import com.mevron.rides.driver.cashout.ui.state.AddFundState
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
class BalanceViewModel @Inject constructor(
    private val linkCase: GetPaymentLinkUseCase,
    private val useCase: GetWalletDetailsUseCase,
    private val cashOutUseCase: CashOutUseCase,
    private val addFundUseCase: AddFundUseCase,
    private val getCardsUseCase: GetCardsUseCase,
    private val confirmUseCase: ConfirmPaymentUseCase
) :
    ViewModel() {

    private val mutableState: MutableStateFlow<GetWalletDetailState> =
        MutableStateFlow(GetWalletDetailState.EMPTY)

    private val addFundMutableState: MutableStateFlow<AddFundState> =
        MutableStateFlow(AddFundState.EMPTY)

    val state: StateFlow<GetWalletDetailState>
        get() = mutableState

    private fun getWalletDetails() {
        updateState(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase()) {
                is DomainModel.Success -> {
                    val data = result.data as PaymentDetailsDomainData
                    updateState(
                        loading = false,
                        errorMessage = "",
                        balance = data.balance,
                        date = data.nextPaymentDate
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

    private fun cashOutWallet() {
        updateState(loading = true)
        val balance = mutableState.value.cashOutAmount
        if (balance.isEmpty()) {
            updateState(loading = false)
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

   private fun addFundToWallet() {
        updateState(loading = true)
        val balance = mutableState.value.cashOutAmount
        val cardNumber = mutableState.value.cardNumber
        if (balance.isEmpty() || cardNumber.isEmpty()) {
           // updateState(loading = false)
          //  return
        }
        val data = mutableState.value.toCardRequest()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = addFundUseCase(data =   CashActionData(
                amount = balance,
                card_id = cardNumber
            ))) {
                is DomainModel.Success -> {
                    updateState(
                        loading = false,
                        errorMessage = "",
                        successFund = true
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        loading = false,
                        errorMessage = "Failure to add Fund",
                    )
                }
            }
        }
    }

    private fun getCards() {
        updateState(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getCardsUseCase()) {
                is DomainModel.Success -> {
                    val dd = result.data as GetCardData
                    Log.d("THE CARDS ARE", "THE CARDS ARE 2 $dd")
                    updateState(
                        loading = false,
                        errorMessage = "",
                        successCard = true,
                        cardData = dd
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        loading = false,
                        errorMessage = "Failure to get card",
                    )
                }
            }
        }
    }


    private fun GetWalletDetailState.toRequest(): CashActionData =
        CashActionData(
            amount = this.balance,
            card_id = null
        )

    private fun GetWalletDetailState.toCardRequest(): CashActionData =
        CashActionData(
            amount = this.balance,
            card_id = this.cardNumber
        )

    fun onEvent(event: CashOutAddFundEvent) {
        when (event) {
            CashOutAddFundEvent.OnCashOutClick -> cashOutWallet()
            CashOutAddFundEvent.AddFundClicked -> addFundToWallet()
            CashOutAddFundEvent.GetCards -> getCards()
            CashOutAddFundEvent.GetWalletDetail -> getWalletDetails()
        }
    }

    fun getPayLink() {
        updateState(loading = true)
        val request = mutableState.value.toLinkRequest()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = linkCase(request)) {
                is DomainModel.Success -> {
                    val data = result.data as PaymentLinkDomain
                    updateState(
                        loading = false,
                        payLink = data.link
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        loading = false,
                        errorLink = "Failure to get payment link"
                    )
                }
            }
        }
    }

    private fun GetWalletDetailState.toLinkRequest(): GetLinkAmount =
        GetLinkAmount(
            amount = this.addFundAmount
        )

    private fun createSection(data: PaymentDetailsDomainData) {
        val arr = data.data.sortedByDescending {
            it.date.replace("-", "")
        }
        val arr1 = mutableListOf<PaymentBalanceDetailsDomainDatum>()
        var arr2 = mutableListOf<PaymentDetailsDomainDatum>()
        var headingDate = ""

        for (i in arr.indices) {
            if (i == 0) {
                arr[i].apply {
                    headingDate = this.date.toReadableDate()
                    arr2.add(PaymentDetailsDomainDatum(amount, date, icon, method, narration, time))
                }
            } else {
                if (arr[i - 1].date == arr[i].date) {
                    arr[i].apply {
                        headingDate = this.date.toReadableDate()
                        arr2.add(
                            PaymentDetailsDomainDatum(
                                amount,
                                date,
                                icon,
                                method,
                                narration,
                                time
                            )
                        )
                    }
                } else {
                    arr1.add(PaymentBalanceDetailsDomainDatum(date = headingDate, data = arr2))
                    arr2 = mutableListOf()
                    arr[i].apply {
                        headingDate = this.date.toReadableDate()
                        arr2.add(
                            PaymentDetailsDomainDatum(
                                amount,
                                date,
                                icon,
                                method,
                                narration,
                                time
                            )
                        )
                    }
                }
            }
            if (i == arr.size - 1) {
                arr1.add(PaymentBalanceDetailsDomainDatum(date = headingDate, data = arr2))
            }
        }
        updateState(data = arr1)
    }

    private fun String.toReadableDate(): String {
        val theDate = this.split("-")
        return "${theDate[1].toMonth()} ${theDate[2]}, ${theDate[0]}"
    }

    private fun String.toMonth(): String {
        if (this == "01") {
            return "January"
        }
        if (this == "02") {
            return "February"
        }
        if (this == "03") {
            return "March"
        }
        if (this == "04") {
            return "April"
        }
        if (this == "05") {
            return "May"
        }
        if (this == "06") {
            return "June"
        }
        if (this == "07") {
            return "July"
        }
        if (this == "08") {
            return "August"
        }
        if (this == "09") {
            return "September"
        }
        if (this == "10") {
            return "October"
        }
        if (this == "11") {
            return "November"
        }
        if (this == "12") {
            return "December"
        }
        return ""
    }

    fun confirmPayment() {
        val uuid = mutableState.value.confirmLink
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = confirmUseCase(uuid)) {
                else -> {
                    updateState(
                        loading = false,
                        error = "",
                        successFund = true
                    )
                }
            }
        }
    }

    fun updateState(
        loading: Boolean? = null,
        errorMessage: String? = null,
        balance: String? = null,
        success: Boolean? = null,
        data: List<PaymentBalanceDetailsDomainDatum>? = null,
        date: String? = null,
        cashOut: String? = null,
        addFund: String? = null,
        cardNumber: String? = null,
        cardData: GetCardData? = null,
        successFund: Boolean? = null,
        successCard: Boolean? = null,
        error: String? = null,
        payLink: String? = null,
        addCard: Boolean? = null,
        shouldGoBack: Boolean? = null,
        confirmLink: String? = null,
    ) {
        val currentState = mutableState.value
        val addFundState = addFundMutableState.value
        mutableState.update {
            currentState.copy(
                loading = loading ?: currentState.loading,
                errorMessage = errorMessage ?: currentState.errorMessage,
                balance = balance ?: currentState.balance,
                success = success ?: currentState.success,
                data = data ?: currentState.data,
                date = date ?: currentState.date,
                cashOutAmount = cashOut ?: currentState.cashOutAmount,
                addFundAmount = addFund ?: currentState.addFundAmount,
                cardNumber = cardNumber ?: currentState.cardNumber,
                cardData = cardData?.cardData ?: currentState.cardData,
                successFund = successFund ?: currentState.successFund,
                successCard = successCard ?: currentState.successCard,
                errorLink = error ?: currentState.errorLink,
                payLink = payLink ?: currentState.payLink,
                addCard = addCard ?: currentState.addCard,
                shouldGoBack = shouldGoBack ?: currentState.shouldGoBack,
                confirmLink = confirmLink ?: currentState.confirmLink
            )
        }
        addFundMutableState.update {
            addFundState.copy(
                amount = addFund ?: addFundState.amount
            )
        }
    }
}