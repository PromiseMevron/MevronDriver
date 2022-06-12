package com.mevron.rides.driver.home.domain.model

import com.mevron.rides.driver.home.map.widgets.AcceptRideData
import com.mevron.rides.driver.home.ui.ApproachingPassengerData
import com.mevron.rides.driver.home.ui.EmergencyData
import com.mevron.rides.driver.home.ui.GoingToDestinationData
import com.mevron.rides.driver.home.ui.StartRideData

sealed interface MapTripState {
    data class EmergencyState(val data: EmergencyData) : MapTripState
    data class GoingToDestinationState(val data: GoingToDestinationData) : MapTripState
    data class StartRideState(val data: StartRideData) : MapTripState
    data class ApproachingPassengerState(val data: ApproachingPassengerData) : MapTripState
    data class AcceptRideState(val data: AcceptRideData) : MapTripState
    object Idle : MapTripState
}