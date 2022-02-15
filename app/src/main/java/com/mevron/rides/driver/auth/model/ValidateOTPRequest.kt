package com.mevron.rides.driver.auth.model

data class ValidateOTPRequest(
    val code: String,
    val phoneNumber: String
)