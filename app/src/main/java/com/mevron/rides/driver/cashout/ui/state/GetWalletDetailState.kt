package com.mevron.rides.driver.cashout.ui.state

import com.mevron.rides.driver.cashout.domain.model.GetCardData
import com.mevron.rides.driver.cashout.domain.model.PaymentBalanceDetailsDomainDatum
import com.mevron.rides.driver.remote.model.getcard.CardData

data class GetWalletDetailState(
    val loading: Boolean,
    val balance: String,
    val cashOutAmount: String,
    val addFundAmount: String,
    val cardNumber: String,
    val date: String,
    val errorMessage: String,
    val success: Boolean,
    val data: List<PaymentBalanceDetailsDomainDatum>,
    val cardData: List<GetCardData.GetCardDatum>
){
    companion object {
        val EMPTY = GetWalletDetailState(
            loading = false,
            balance = "",
            errorMessage = "",
            success = false,
            data = mutableListOf(),
            date = "",
            cashOutAmount = "0",
            addFundAmount = "0",
            cardNumber = "",
            cardData = mutableListOf()
        )
    }
}
