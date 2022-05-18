package com.mevron.rides.driver.location.domain.repository

import com.mevron.rides.driver.location.domain.model.LocationData
import kotlinx.coroutines.flow.StateFlow

interface IAppLocationManager {
    fun requestLastLocation()
    fun startLocationUpdates()
    fun stopLocationUpdates()
    val receivingLocationUpdates: StateFlow<Boolean>
    val lastLocationData: StateFlow<LocationData?>
}