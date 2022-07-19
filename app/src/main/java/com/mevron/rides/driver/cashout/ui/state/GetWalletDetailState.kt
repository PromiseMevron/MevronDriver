package com.mevron.rides.driver.cashout.ui.state

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
    val successCash: Boolean,
    val successFund: Boolean,
    val successCard: Boolean,
    val data: List<PaymentBalanceDetailsDomainDatum>,
    val cardData: List<CardData>
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
            cardData = mutableListOf(),
            successCash = false,
            successFund = false,
            successCard = false
        )
    }
}
