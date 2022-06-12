package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.IMapStateRepository
import com.mevron.rides.driver.home.domain.model.MapTripState
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class SetMapTripStateUseCaseTest {

    private val repository: IMapStateRepository = mockk(relaxed = true)
    private val useCase = SetMapTripStateUseCase(repository)

    @Test
    fun `on invoke - calls sets current state on repository`() {
        useCase(MapTripState.Idle)
        verify { repository.setCurrentState(MapTripState.Idle) }
    }
}