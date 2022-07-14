package com.mevron.rides.driver.sidemenu.settingsandprofile.ui.event

sealed interface SettingsProfileEvent{
    object FetchFromApi: SettingsProfileEvent
    object UpdateProfile: SettingsProfileEvent
    object SignOut: SettingsProfileEvent
}