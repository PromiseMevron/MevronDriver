package com.mevron.rides.driver.auth.model

data class CreateAccountRequest(
    val city: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    var referralCode: String? = null
)