package com.mevron.rides.driver.cashout.data.model

data class PayDataDatum(
    val amount: String,
    val date: String,
    val icon: String,
    val method: String,
    val narration: String,
    val time: String
)