package com.mevron.rides.driver.location.domain.repository

import com.mevron.rides.driver.location.domain.model.LocationData
import kotlinx.coroutines.flow.StateFlow

interface ILocationRepository {
    val isReceivingLocationUpdates: StateFlow<Boolean>
    fun sendLocations(locations: List<LocationData>)
    fun startLocationUpdates()
    fun stopLocationUpdates()
    val lastLocation: StateFlow<LocationData>
    val liveLocation: StateFlow<LocationData?>
}