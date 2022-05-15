package com.mevron.rides.driver.location.data

import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.location.domain.repository.IAppLocationManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.concurrent.ExecutorService
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test

// TODO Complete this Unit Test when all is done.

class LocationRepositoryTest {
    private val locationManager: IAppLocationManager = mockk(relaxed = true)
    private val socketManager: ISocketManager = mockk()
    private val executor: ExecutorService = mockk()

    private val repository = LocationRepository(locationManager, socketManager, executor)

    @Test
    fun `onStartLocationUpdate - starts location update`() {
        every { locationManager.receivingLocationUpdates }.returns(MutableStateFlow(false))
        every { locationManager.lastLocationData }.returns(MutableStateFlow(null))
        repository.startLocationUpdates()
        verify { locationManager.startLocationUpdates() }
    }

    @Test
    fun `onStopLocationUpdate - stops location update`() {
        every { locationManager.receivingLocationUpdates }.returns(mockk())
        every { locationManager.lastLocationData }.returns(mockk())
        repository.stopLocationUpdates()
        verify { locationManager.stopLocationUpdates() }
    }
}