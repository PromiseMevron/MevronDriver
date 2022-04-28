package com.mevron.rides.driver.updateprofile.domain.usecase.carproperties

import com.mevron.rides.driver.updateprofile.domain.repository.ICarPropertiesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetCarMakesUseCaseTest {

    private val repository: ICarPropertiesRepository = mockk()
    private val useCase = GetCarMakesUseCase(repository)

    @Test
    fun `when GetCarMakesUseCase is invoked - returns CarMakes from repository`(): Unit =
        runBlocking {
            coEvery { repository.getCarMakes() }.coAnswers { mockk() }
            useCase()
            coVerify { repository.getCarMakes() }
        }
}