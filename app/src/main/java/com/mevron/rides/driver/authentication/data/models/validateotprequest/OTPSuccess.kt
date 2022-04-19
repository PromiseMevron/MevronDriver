package com.mevron.rides.driver.authentication.data.models.validateotprequest

import com.google.gson.annotations.SerializedName

data class OTPSuccess(
    @SerializedName("data")
    val otpData: OTPData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)