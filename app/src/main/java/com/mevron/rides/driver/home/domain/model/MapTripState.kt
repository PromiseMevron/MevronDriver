package com.mevron.rides.driver.home.domain.model

import com.mevron.rides.driver.home.map.widgets.AcceptRideData
import com.mevron.rides.driver.home.ui.*

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

    data class  Payment(val data: PayData) : MapTripState {
        override var tripId: String? = null
    }

    data class  Rating(val data: PayData) : MapTripState {
        override var tripId: String? = null
    }
}