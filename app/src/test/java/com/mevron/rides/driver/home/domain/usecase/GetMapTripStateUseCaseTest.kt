package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.IMapStateRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class GetMapTripStateUseCaseTest {

    private val repository: IMapStateRepository = mockk(relaxed = true)
    private val useCase = GetMapTripStateUseCase(repository)

    @Test
    fun `when useCase is invoked - retrieves map state from repository`(): Unit {
        useCase()
        verify { repository.currentMapTripState }
    }
}