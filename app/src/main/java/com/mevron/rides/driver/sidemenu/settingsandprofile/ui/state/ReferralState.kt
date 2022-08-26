package com.mevron.rides.driver.sidemenu.settingsandprofile.ui.state

import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.ReferralData

data class ReferralState(
    val isLoading: Boolean,
    val setReferal: Boolean,
    val numberOfRides: String,
    val referalID: String,
    val referralStatus: Int,
    val referralCode: String,
    val refData: List<ReferralData>,
    val setCode: String,
    val error: String,
    val succes: Boolean,
    val startDate: String,
    val endDate: String
) {
    companion object {
        val EMPTY = ReferralState(
            isLoading = false,
            setReferal = false,
            numberOfRides = "",
            referralStatus = 0,
            referralCode = "",
            refData = mutableListOf(),
            referalID = "",
            setCode = "",
            error = "",
            succes = false,
            startDate = "",
            endDate = ""
        )
    }
}

