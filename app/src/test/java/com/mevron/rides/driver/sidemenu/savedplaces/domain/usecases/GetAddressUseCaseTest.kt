package com.mevron.rides.driver.sidemenu.savedplaces.domain.usecases

import com.mevron.rides.driver.sidemenu.savedplaces.domain.repository.IAddressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAddressUseCaseTest {

    private val repository: IAddressRepository = mockk()
    private val useCase = GetAddressUseCase(repository)

    @Test
    fun `on invoke repository gets address`(): Unit = runBlocking {
        coEvery { repository.getSavedAddresses() }.coAnswers { mockk() }
        useCase()
        coVerify { repository.getSavedAddresses() }
    }
}