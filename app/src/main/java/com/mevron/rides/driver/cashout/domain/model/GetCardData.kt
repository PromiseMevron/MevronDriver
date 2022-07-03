package com.mevron.rides.driver.cashout.domain.model



data class GetCardData(val cardData: List<GetCardDatum>) {

    data class GetCardDatum(
        val bin: String,
        val brand: String,
        val expiryMonth: String,
        val expiryYear: String,
        val lastDigits: String,
        val type: String,
        val uuid: String
    )
}