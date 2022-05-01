package com.mevron.rides.driver.updateprofile.ui.event

import com.mevron.rides.driver.updateprofile.domain.model.SecurityNumRequest

data class AddSocialSecurityNumberState(
    val socialSecurityNumber: String,
    val isLoading: Boolean,
    val shouldRouteBack: Boolean,
    val isRequestSuccess: Boolean,
    val error: AddSocialSecurityNumberError
) {

    fun buildRequest(): SecurityNumRequest = SecurityNumRequest(securityNumber = socialSecurityNumber)

    val isButtonEnabled: Boolean
        get() = socialSecurityNumber.isNotEmpty()

    companion object {
        val DEFAULT = AddSocialSecurityNumberState(
            socialSecurityNumber = "",
            isLoading = false,
            shouldRouteBack = false,
            error = AddSocialSecurityNumberError.None,
            isRequestSuccess = false
        )
    }
}

sealed interface AddSocialSecurityNumberError {
    object None : AddSocialSecurityNumberError
    data class RequestError(val message: String) : AddSocialSecurityNumberError
}