package com.mevron.rides.driver.sidemenu.settingsandprofile.ui.state

import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.GetProfileData

data class SettingsProfileState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val error: String,
    val profile: GetProfileData
) {
    companion object {
        val EMPTY = SettingsProfileState(
            isLoading = false,
            isSuccess = false,
            error = "",
            profile = GetProfileData()
        )
    }
}
