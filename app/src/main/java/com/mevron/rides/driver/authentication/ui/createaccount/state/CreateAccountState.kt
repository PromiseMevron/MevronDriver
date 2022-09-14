package com.mevron.rides.driver.authentication.ui.createaccount.state

import com.mevron.rides.driver.authentication.domain.model.GetCitiesData

data class CreateAccountState(
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val city: String,
    val cityName: String,
    val cityData: List<GetCitiesData>,
    val referral: String?,
    val isLoading: Boolean,
    val isRequestSuccess: Boolean,
    val email: String,
    val error: String,
    val detailsComplete: Boolean,
    val validEmail: Boolean,
    val country: String,
    val type: String
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
            validEmail = false,
            country = "",
            cityData = mutableListOf(),
            cityName = "",
            type = ""
        )
    }
}
