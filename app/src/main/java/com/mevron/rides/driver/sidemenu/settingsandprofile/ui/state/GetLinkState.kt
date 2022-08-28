package com.mevron.rides.driver.sidemenu.settingsandprofile.ui.state

import com.mevron.rides.driver.cashout.domain.model.GetBankDatum
import com.mevron.rides.driver.cashout.domain.model.GetCardData
import com.mevron.rides.driver.remote.model.getcard.CardData
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.payment.Bank

data class GetLinkState(
    val paymentLink: String,
    val error: String,
    val isLoading: Boolean,
    val amount: String,
    val confirmLink: String,
    val successFund: Boolean,
    val cardData: List<CardData>,
    val bankData: List<GetBankDatum>,
    val addCard: Boolean,

    ) {
    companion object {
        val EMPTY = GetLinkState(
            paymentLink = "",
            error = "",
            isLoading = false,
            amount = "0",
            confirmLink = "",
            successFund = false,
            cardData = mutableListOf(),
            addCard = false,
            bankData = mutableListOf()
        )
    }
}