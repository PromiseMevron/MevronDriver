package com.mevron.rides.driver.home.ui.event

import com.mevron.rides.driver.remote.TripManagementModel

sealed interface HomeViewEvent {
    object OnToggleOnlineClick : HomeViewEvent
    object OnDocumentSubmissionStatusClick : HomeViewEvent
    object OnDriveClick : HomeViewEvent
    object OnEarningClick : HomeViewEvent
    object AcceptRideClick : HomeViewEvent
    object StartRideClick : HomeViewEvent
    data class LocationStarted(val isStarted: Boolean) : HomeViewEvent
}