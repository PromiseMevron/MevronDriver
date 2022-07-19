package com.mevron.rides.driver.cashout.domain.model

import com.mevron.rides.driver.remote.model.getcard.CardData


data class GetCardData(val cardData: List<CardData>) {

    data class GetCardDatum(
        val bin: String?,
        val brand: String,
        val expiryMonth: String,
        val expiryYear: String,
        val lastDigits: String,
        val type: String,
        val uuid: String
    )
}