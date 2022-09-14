package com.mevron.rides.driver.authentication.domain.model

data class CreateAccountRequest(
    val city: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val type: String,
    var referralCode: String? = null
)