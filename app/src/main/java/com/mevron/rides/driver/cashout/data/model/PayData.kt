package com.mevron.rides.driver.cashout.data.model

import com.google.gson.annotations.SerializedName

data class PayData(
    val balance: String,
    @SerializedName("currency_symbol")
    val currencySymbol: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("data")
    val payData: List<PayDataDatum>,
    val nextPaymentDate: String
)