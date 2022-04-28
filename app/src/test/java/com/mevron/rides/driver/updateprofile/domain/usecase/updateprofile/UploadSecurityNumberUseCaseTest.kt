package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.model.SecurityNumRequest
import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class UploadSecurityNumberUseCaseTest {

    private val repository: IUpdateProfileRepository = mockk()
    private val useCase = UploadSecurityNumberUseCase(repository)

    @Test
    fun `when UploadSecurityNumberUseCase is invoked - repository uploads security number`(): Unit =
        runBlocking {
            coEvery { repository.uploadSecurityNumber(any()) }.coAnswers { mockk() }
            val request: SecurityNumRequest = mockk()
            useCase(request)
            coVerify { repository.uploadSecurityNumber(request) }
        }
}