package com.mevron.rides.driver.authentication.data.models.validateotprequest

import com.google.gson.annotations.SerializedName

data class VerifyOTPResponse(
    @SerializedName("success")
    val otpSuccess: OTPSuccess
)