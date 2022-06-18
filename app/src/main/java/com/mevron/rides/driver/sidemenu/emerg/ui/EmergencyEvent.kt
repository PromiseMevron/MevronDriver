package com.mevron.rides.driver.sidemenu.emerg.ui

sealed interface EmergencyEvent{
    object OpenNextPage: EmergencyEvent
    object MakeAPICall: EmergencyEvent
    object OnBackButtonClicked: EmergencyEvent
    object DeleteContact: EmergencyEvent
    object UpdateContact: EmergencyEvent
}