package com.mevron.rides.driver.authentication.domain.model

import com.mevron.rides.driver.authentication.data.models.registerphone.PhoneCodeData

data class RegisterPhoneDomainData(
    val phoneCodeData: PhoneCodeData,
    val message: String,
    val status: String
)