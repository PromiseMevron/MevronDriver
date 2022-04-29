package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.model.AddVehicleRequest
import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddVehicleUseCaseTest {

    private val repository: IUpdateProfileRepository = mockk()
    private val useCase = AddVehicleUseCase(repository)

    @Test
    fun `when AddVehicleUseCase is invoked - calls repository add vehicle`(): Unit =
        runBlocking {
            coEvery { repository.addVehicle(any()) }.coAnswers { mockk() }
            val request: AddVehicleRequest = mockk()
            useCase(request)
            coVerify { repository.addVehicle(request) }
        }
}