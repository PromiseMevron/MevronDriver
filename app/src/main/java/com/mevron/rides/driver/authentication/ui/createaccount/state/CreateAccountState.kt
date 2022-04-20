package com.mevron.rides.driver.authentication.ui.createaccount.state

data class CreateAccountState(
    val phoneNumber: String,
    val name: String,
    val city: String,
    val referral: String,
    val isLoading: Boolean
) {
    companion object {
        val EMPTY = CreateAccountState(
            phoneNumber = "",
            name = "",
            city = "",
            referral = "",
            isLoading = false
        )
    }
}
