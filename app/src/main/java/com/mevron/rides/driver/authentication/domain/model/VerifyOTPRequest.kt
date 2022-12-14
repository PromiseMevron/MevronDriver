package com.mevron.rides.driver.authentication.domain.model

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)