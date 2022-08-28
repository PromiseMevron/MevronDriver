package com.mevron.rides.driver.sidemenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.cashout.data.model.GetLinkAmount
import com.mevron.rides.driver.cashout.domain.model.GetCardData
import com.mevron.rides.driver.cashout.domain.model.PaymentLinkDomain
import com.mevron.rides.driver.cashout.domain.usecase.ConfirmPaymentUseCase
import com.mevron.rides.driver.cashout.domain.usecase.GetCardsUseCase
import com.mevron.rides.driver.cashout.domain.usecase.GetPaymentLinkUseCase
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.remote.MevronRepo
import com.mevron.rides.driver.remote.model.getcard.AddCard
import com.mevron.rides.driver.remote.model.getcard.CardData
import com.mevron.rides.driver.remote.model.getcard.GetCardResponse
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.state.GetLinkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor (private val useCase: GetPaymentLinkUseCase,
                                             private val getCardsUseCase: GetCardsUseCase,private  val confirmUseCase: ConfirmPaymentUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<GetLinkState> =
        MutableStateFlow(GetLinkState.EMPTY)

    val state: StateFlow<GetLinkState>
        get() = mutableState

    fun getPayLink() {
        updateState(isLoading = true)
      //  val request = mutableState.value.toRequest()
        viewModelScope.launch(Dispatchers.IO) {

            when (val result = useCase(GetLinkAmount("100"))) {
                is DomainModel.Success -> {
                    val data = result.data as PaymentLinkDomain
                    updateState(
                        isLoading = false,
                        payLink = data.link
                    )
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        error = result.buildString()
                    )
                }
            }
        }
    }

    private fun DomainModel.Error.buildString(): String =
        this.error.localizedMessage ?: "Card Addition Failed"




    fun getPaymentMethods() {
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCardsUseCase()
            if (result is DomainModel.Success) {
                val dd = result.data as GetCardData
                updateState(paymentCards = dd, isLoading = false)
            } else {
                updateState(error = (result as DomainModel.Error).error.toString())
            }
        }
    }

    private fun GetLinkState.toRequest(): GetLinkAmount =
        GetLinkAmount(
            amount = this.amount
        )

    fun confirmPayment() {
        val uuid = mutableState.value.confirmLink
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = confirmUseCase(uuid)) {
                else -> {
                    updateState(
                        isLoading = false,
                        error = "",
                        successFund = true
                    )
                }
            }
        }
    }


    fun updateState(
        payLink: String? = null,
        isLoading: Boolean? = null,
        error: String? = null,
        amount: String? = null,
        paymentCards: GetCardData? = null,
        confirmLink: String? = null,
        successFund: Boolean? = null,
        addCard: Boolean? = null
    ) {
        val currentValue = mutableState.value
        mutableState.update {
            currentValue.copy(
                paymentLink = payLink ?: currentValue.paymentLink,
                isLoading = isLoading ?: currentValue.isLoading,
                error = error ?: currentValue.error,
                amount = amount ?: currentValue.amount,
                cardData = paymentCards?.cardData ?: currentValue.cardData,
                confirmLink = confirmLink ?: currentValue.confirmLink,
                successFund = successFund ?: currentValue.successFund,
                addCard = addCard ?: currentValue.addCard,
                bankData = paymentCards?.bankData ?: currentValue.bankData
            )
        }
    }
}