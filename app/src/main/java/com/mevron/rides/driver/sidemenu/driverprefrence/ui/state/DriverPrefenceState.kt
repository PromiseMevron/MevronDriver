package com.mevron.rides.driver.sidemenu.driverprefrence.ui.state

data class DriverPrefenceState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val acceptCash: Int,
    val acceptTrips: Int,
    val excludeLowRated: Int,
    val longDistance: Int,
    val preferredNavigation: String,
    val isPostSuccess: Boolean,
    val errorPost: String,
    val error: String,
) {
    companion object {
        val EMPTY = DriverPrefenceState(
            isLoading = false,
            isSuccess = false,
            error = "",
            errorPost = "",
            isPostSuccess = false,
            acceptCash = 0,
            acceptTrips = 0,
            excludeLowRated = 0,
            longDistance = 0,
            preferredNavigation = "maps"

        )
    }
}
