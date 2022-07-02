package com.mevron.rides.driver.cashout.ui.state

import com.mevron.rides.driver.cashout.domain.model.PaymentBalanceDetailsDomainDatum

data class GetWalletDetailState(
    val loading: Boolean,
    val balance: String,
    val cashOutAmount: String,
    val addFundAmount: String,
    val date: String,
    val errorMessage: String,
    val success: Boolean,
    val data: List<PaymentBalanceDetailsDomainDatum>

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
            addFundAmount = "0"
        )
    }
}
