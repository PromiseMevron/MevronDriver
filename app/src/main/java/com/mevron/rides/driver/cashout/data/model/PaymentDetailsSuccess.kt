package com.mevron.rides.driver.cashout.data.model

import com.google.gson.annotations.SerializedName

data class PaymentDetailsSuccess(
    @SerializedName("data")
    val payData: PayData,
    val message: String,
    val status: String
)