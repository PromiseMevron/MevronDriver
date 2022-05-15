package com.mevron.rides.driver.location.data

import android.util.Log
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.location.domain.model.LocationData
import com.mevron.rides.driver.location.domain.repository.IAppLocationManager
import com.mevron.rides.driver.location.domain.repository.ILocationRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "LocationRepository_"

class LocationRepository(
    private val locationManager: IAppLocationManager,
    private val socketManager: ISocketManager,
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
) : ILocationRepository {

    private fun sendLocation(location: LocationData) {
        Log.d(TAG, "Sending Location $location")
        if (location.isForeground) {
            executor.execute {
                // socketManager.emit(location)
            }
        } else {
            // api send location
        }
    }

    /**
     * Adds list of locations to the database.
     */
    override fun sendLocations(locations: List<LocationData>) {
        executor.execute { locations.forEach(::sendLocation) }
    }

    // Location related fields/methods:
    /**
     * Status of whether the app is actively subscribed to location changes.
     */
    override val receivingLocationUpdates: StateFlow<Boolean> =
        locationManager.receivingLocationUpdates

    /**
     * Subscribes to location updates.
     */
    override fun startLocationUpdates() = locationManager.startLocationUpdates()

    /**
     * Un-subscribes from location updates.
     */
    override fun stopLocationUpdates() = locationManager.stopLocationUpdates()

    override val lastLocation: StateFlow<LocationData?> = locationManager.lastLocationData
}