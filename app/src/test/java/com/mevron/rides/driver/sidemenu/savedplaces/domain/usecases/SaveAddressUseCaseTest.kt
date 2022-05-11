package com.mevron.rides.driver.sidemenu.savedplaces.domain.usecases

import com.mevron.rides.driver.sidemenu.savedplaces.data.model.SaveAddressRequest
import com.mevron.rides.driver.sidemenu.savedplaces.domain.repository.IAddressRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SaveAddressUseCaseTest {

    private val repository: IAddressRepository = mockk()
    private val useCase = SaveAddressUseCase(repository)

    @Test
    fun `on invoke repository gets address`(): Unit = runBlocking {
        coEvery { repository.addAnAddress(any()) }.coAnswers { mockk() }
        val request = SaveAddressRequest(
            address = "LL",
            latitude = "ss",
            longitude = "ss",
            name = "oo",
            type = "sss"
        )
        useCase(request)
        coVerify { repository.addAnAddress(request) }
    }
}