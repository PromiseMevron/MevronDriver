package com.mevron.rides.driver.home.domain.model

object State {
    const val ORDER = "ORDER"
    const val IN_TRIP = "INTRIP"
    const val PAYMENT = "PAYMENT"
    const val RATING = "RATING"
}

object InTripState {
    const val APPROACHING_PASSENGER = "APPROACHING_PASSENGER"
    const val DRIVER_ARRIVED = "DRIVER_ARRIVED"
    const val GOING_TO_DESTINATION = "GOING_TO_DESTINATION"
}

enum class StateMachineCurrentState(val state: String) {
    ORDER("ORDER"),
    IN_TRIP("INTRIP"),
    PAYMENT("PAYMENT"),
    UNKNOWN("UNKNOWN"),
    RATING("RATING");

    companion object {
        fun from(string: String): StateMachineCurrentState =
            when (string) {
                State.ORDER -> ORDER
                State.IN_TRIP -> IN_TRIP
                State.PAYMENT -> PAYMENT
                State.RATING -> RATING
                else -> UNKNOWN
            }
    }
}

enum class InTripStateMachineCurrentState(val state: String) {
    APPROACHING_PASSENGER("APPROACHING_PASSENGER"),
    DRIVER_ARRIVED("DRIVER_ARRIVED"),
    GOING_TO_DESTINATION("GOING_TO_DESTINATION"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun from(string: String?): InTripStateMachineCurrentState =
            when (string) {
                InTripState.APPROACHING_PASSENGER -> APPROACHING_PASSENGER
                InTripState.DRIVER_ARRIVED -> DRIVER_ARRIVED
                InTripState.GOING_TO_DESTINATION -> GOING_TO_DESTINATION
                else -> UNKNOWN
            }
    }
}