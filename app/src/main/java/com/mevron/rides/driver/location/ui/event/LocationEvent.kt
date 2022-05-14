package com.mevron.rides.driver.location.ui.event

sealed interface LocationEvent {
    object StartLocationUpdate : LocationEvent
    object StopLocationUpdate : LocationEvent
}