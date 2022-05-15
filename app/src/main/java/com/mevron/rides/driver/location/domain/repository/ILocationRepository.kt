package com.mevron.rides.driver.location.domain.repository

import androidx.annotation.MainThread
import com.mevron.rides.driver.location.domain.model.LocationData
import kotlinx.coroutines.flow.StateFlow

interface ILocationRepository {
    val receivingLocationUpdates: StateFlow<Boolean>
    fun sendLocations(locations: List<LocationData>)

    fun startLocationUpdates()

    fun stopLocationUpdates()

    val lastLocation: StateFlow<LocationData?>
}