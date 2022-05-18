package com.mevron.rides.driver.location.domain.usecase

import app.cash.turbine.test
import com.mevron.rides.driver.location.domain.model.LocationData
import com.mevron.rides.driver.location.domain.repository.ILocationRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class UserCurrentLocationUseCaseTest {

    private val locationRepository: ILocationRepository = mockk(relaxed = true)
    private val useCase = UserCurrentLocationUseCase(locationRepository)

    @Test
    fun `when locationRepository emit last location - useCase emit data`(): Unit = runBlocking {
        val testLocationData = LocationData(latitude = 12.0)
        every { locationRepository.lastLocation }.returns(MutableStateFlow(testLocationData))
        useCase().test {
            assertEquals(awaitItem(), testLocationData)
        }
    }
}