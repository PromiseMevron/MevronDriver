package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.IHomeScreenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetDocumentStatusUseCaseTest {

    private val repository: IHomeScreenRepository = mockk()
    private val useCase = GetDocumentStatusUseCase(repository)

    @Test
    fun `on invoke - returns status from repository`(): Unit = runBlocking {
        coEvery { repository.getDocumentStatus() }.coAnswers { mockk() }
        useCase()
        coVerify { repository.getDocumentStatus() }
    }
}