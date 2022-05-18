package com.mevron.rides.driver.home.ui.event

sealed interface HomeViewEvent {
    object OnToggleOnlineClick : HomeViewEvent
    object OnDocumentSubmissionStatusClick: HomeViewEvent
    object OnDriveClick: HomeViewEvent
    object OnEarningClick: HomeViewEvent
    data class LocationStarted(val isStarted: Boolean) : HomeViewEvent
}