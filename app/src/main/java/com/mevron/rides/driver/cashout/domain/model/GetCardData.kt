package com.mevron.rides.driver.cashout.domain.model

import android.os.Parcelable
import com.mevron.rides.driver.remote.model.getcard.CardData
import kotlinx.parcelize.Parcelize


data class GetCardData(val cardData: List<CardData>, val bankData: List<GetBankDatum>)

@Parcelize
data class GetBankDatum(
    val account_number: String,
    val bank_name: String,
    val bank_code: String,
    val default: Boolean,
    val uuid: String,
    val account_name: String
): Parcelable