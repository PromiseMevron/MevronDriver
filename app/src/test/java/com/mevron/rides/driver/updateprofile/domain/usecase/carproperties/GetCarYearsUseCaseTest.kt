package com.mevron.rides.driver.updateprofile.domain.usecase.carproperties

import com.mevron.rides.driver.updateprofile.domain.repository.ICarPropertiesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetCarYearsUseCaseTest {

    private val repository: ICarPropertiesRepository = mockk()
    private val useCase = GetCarYearsUseCase(repository)

    @Test
    fun `when GetCarYearsUseCase is invoked - returns CarYears from repository`(): Unit =
        runBlocking {
            coEvery { repository.getCarYears(any()) }.coAnswers { mockk() }
            useCase(hashMapOf())
            coVerify { repository.getCarYears(hashMapOf()) }
        }
}