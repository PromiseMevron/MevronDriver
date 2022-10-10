package com.mevron.rides.driver.authentication.domain.model

data class VerifyOTPDomainModel(
    val accessToken: String,
    val riderType: String,
    val type: String,
    val uuid: String,
    val proceed: Boolean
)