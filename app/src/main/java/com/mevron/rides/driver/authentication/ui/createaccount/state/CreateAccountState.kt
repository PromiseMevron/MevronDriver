package com.mevron.rides.driver.authentication.ui.createaccount.state

data class CreateAccountState(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val city: String,
    val referral: String?,
    val isLoading: Boolean,
    val isRequestSuccess: Boolean,
    val email: String,
    val error: String,
    val detailsComplete: Boolean,
    val validEmail: Boolean
) {
    companion object {
        val EMPTY = CreateAccountState(
            phoneNumber = "",
            firstName = "",
            lastName = "",
            city = "",
            referral = null,
            isLoading = false,
            isRequestSuccess = false,
            error = "",
            email = "",
            detailsComplete = false,
            validEmail = false
        )
    }
}
