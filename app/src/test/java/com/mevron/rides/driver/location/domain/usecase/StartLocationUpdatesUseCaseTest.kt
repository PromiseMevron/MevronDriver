package com.mevron.rides.driver.location.domain.usecase

import com.mevron.rides.driver.location.domain.repository.ILocationRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class StartLocationUpdatesUseCaseTest {

    private val repository: ILocationRepository = mockk(relaxed = true)
    private val useCase = StopLocationUpdatesUseCase(repository)

    @Test
    fun `when StopLocationUpdatesUseCase is invoked - repository stops location update`() {
        every { repository.stopLocationUpdates() }.just(Runs)
        useCase()
        verify { repository.stopLocationUpdates() }
    }
}