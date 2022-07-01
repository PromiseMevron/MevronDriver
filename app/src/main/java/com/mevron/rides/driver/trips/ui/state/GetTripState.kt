package com.mevron.rides.driver.trips.ui.state

import com.mevron.rides.driver.sidemenu.savedplaces.ui.saveaddress.state.SaveAddressState
import com.mevron.rides.driver.trips.domain.model.GetTripDomainData
import com.mevron.rides.driver.trips.ui.RideActivityDataClass

data class GetTripState(
    val startDate: String,
    val endDate: String,
    val data: GetTripDomainData,
    val items:  MutableList<RideActivityDataClass>,
    val isLoading: Boolean,
    val isRequestSuccess: Boolean,
    val error: String,
    val displayDate: String
) {
    companion object {
        val EMPTY = GetTripState(
            isLoading = false,
            error = "",
            startDate = "",
            data = GetTripDomainData(),
            items = mutableListOf(),
            isRequestSuccess = false,
            endDate = "",
            displayDate = ""
        )
    }
}
