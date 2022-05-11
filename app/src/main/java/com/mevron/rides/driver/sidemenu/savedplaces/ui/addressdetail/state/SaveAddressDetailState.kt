package com.mevron.rides.driver.sidemenu.savedplaces.ui.addressdetail.state

data class SaveAddressDetailState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val error: String,
    val backButton: Boolean,
    val lat: Double,
    val lng: Double,
    val address: String,
    val type: String,
    val name: String,
    val isCorrect: Boolean
) {
    companion object {
        val EMPTY = SaveAddressDetailState(
            isLoading = false,
            isSuccess = false,
            error = "",
            type = "",
            name = "",
            address = "",
            lat = 0.0,
            lng = 0.0,
            backButton = false,
            isCorrect = false
        )
    }
}
