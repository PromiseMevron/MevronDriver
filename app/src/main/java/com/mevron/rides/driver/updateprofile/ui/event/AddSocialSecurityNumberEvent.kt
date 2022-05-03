package com.mevron.rides.driver.updateprofile.ui.event

sealed interface AddSocialSecurityNumberEvent {
    object AddSocialSecurityNumberButtonClick : AddSocialSecurityNumberEvent
    object BackButtonClick : AddSocialSecurityNumberEvent
}