package com.mevron.rides.driver.trips.ui.event

sealed interface GetTripsEvent{
    object GetTrips: GetTripsEvent
}