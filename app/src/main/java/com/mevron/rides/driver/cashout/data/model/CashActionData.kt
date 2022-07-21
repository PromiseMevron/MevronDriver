package com.mevron.rides.driver.cashout.data.model

data class CashActionData(
    val amount: String,
    val card_id: String? = null
)