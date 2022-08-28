package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.payment

data class Card(
    val bin: String,
    val brand: String,
    val expiryMonth: String,
    val expiryYear: String,
    val lastDigits: String,
    val type: String,
    val uuid: String
)