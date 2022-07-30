package com.mevron.rides.driver.home.domain.model

import com.mevron.rides.driver.home.map.widgets.AcceptRideData
import com.mevron.rides.driver.home.ui.ApproachingPassengerData
import com.mevron.rides.driver.home.ui.EmergencyData
import com.mevron.rides.driver.home.ui.GoingToDestinationData
import com.mevron.rides.driver.home.ui.StartRideData

sealed interface MapTripState {
    var tripId: String?

    data class EmergencyState(val data: EmergencyData) : MapTripState {
        override var tripId: String? = null
    }

    data class GoingToDestinationState(val data: GoingToDestinationData) : MapTripState {
        override var tripId: String? = null
    }

    data class StartRideState(val data: StartRideData) : MapTripState {
        override var tripId: String? = null
    }

    data class ApproachingPassengerState(val data: ApproachingPassengerData) : MapTripState {
        override var tripId: String? = null
    }

    data class AcceptRideState(val data: AcceptRideData) : MapTripState {
        override var tripId: String? = null
    }

    object Idle : MapTripState {
        override var tripId: String? = null
    }

    object Payment : MapTripState {
        override var tripId: String? = null
    }
}