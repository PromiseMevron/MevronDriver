package com.mevron.rides.driver.authentication.ui.registerphone.state

data class RegisterPhoneState(
    val loading: Boolean,
    val country: String,
    val countryCode: String,
    val phoneNumber: String,
    val code: String,
    val requestSuccess: Boolean,
    val error: String,
    val phoneCodeExpiration: String,
    val number: String,
    val isValidNumber: Boolean,
    val isCorrectNumber: Boolean
) {

    val countryCodeAndPhoneNumber: String
        get() = "$countryCode$phoneNumber"

    companion object {
        val EMPTY = RegisterPhoneState(
            isCorrectNumber = false,
            loading = false,
            country = "",
            countryCode = "",
            phoneNumber = "",
            code = "",
            requestSuccess = false,
            error = "",
            phoneCodeExpiration = "",
            number = "",
            isValidNumber = false
        )
    }
}
