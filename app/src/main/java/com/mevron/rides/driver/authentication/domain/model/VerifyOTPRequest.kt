package com.mevron.rides.driver.auth.model

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @SerializedName("code")
    val code: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)