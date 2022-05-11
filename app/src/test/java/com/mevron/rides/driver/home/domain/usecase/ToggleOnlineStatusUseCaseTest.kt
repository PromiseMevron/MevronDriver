package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.IHomeScreenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ToggleOnlineStatusUseCaseTest {

    private val repository: IHomeScreenRepository = mockk()
    private val useCase = ToggleOnlineStatusUseCase(repository)

    @Test
    fun `on invoke - toggles status in repository`(): Unit = runBlocking {
        coEvery { repository.toggleStatus() }.coAnswers { mockk() }
        useCase()
        coVerify { repository.toggleStatus() }
    }
}