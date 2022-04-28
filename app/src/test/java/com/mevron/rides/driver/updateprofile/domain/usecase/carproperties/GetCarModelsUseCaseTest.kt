package com.mevron.rides.driver.updateprofile.domain.usecase.carproperties

import com.mevron.rides.driver.updateprofile.domain.repository.ICarPropertiesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetCarModelsUseCaseTest {

    private val repository: ICarPropertiesRepository = mockk()
    private val useCase = GetCarModelsUseCase(repository)

    @Test
    fun `when GetCarModelsUseCase is invoked - calls repository to get Car Models`(): Unit =
        runBlocking{
            coEvery { repository.getCarModels(any()) }.coAnswers { mockk() }
            useCase(hashMapOf())
            coVerify { repository.getCarModels(hashMapOf()) }
        }
}