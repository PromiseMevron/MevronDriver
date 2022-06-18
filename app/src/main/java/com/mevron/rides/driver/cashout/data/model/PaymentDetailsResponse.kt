package com.mevron.rides.driver.cashout.data.model

import com.google.gson.annotations.SerializedName

data class PaymentDetailsResponse(
    @SerializedName("success")
    val paySuccess: PaymentDetailsSuccess
)