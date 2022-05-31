package com.mevron.rides.driver.home.domain.model

object State {
    const val ORDER = "ORDER"
    const val IN_TRIP = "INTRIP"
    const val PAYMENT = "PAYMENT"
}

enum class StateMachineCurrentState(val state: String) {
    ORDER("ORDER"),
    IN_TRIP("INTRIP"),
    PAYMENT("PAYMENT");

    companion object {
        fun from(string: String): StateMachineCurrentState =
            when (string) {
                State.ORDER -> ORDER
                State.IN_TRIP -> IN_TRIP
                State.PAYMENT -> PAYMENT
                else -> throw IllegalArgumentException("Unknown state")
            }
    }
}