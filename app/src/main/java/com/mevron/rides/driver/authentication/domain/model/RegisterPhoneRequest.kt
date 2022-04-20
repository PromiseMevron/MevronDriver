package com.mevron.rides.driver.authentication.domain.model

data class RegisterPhoneRequest(
    val country: String,
    val phoneNumber: String
)