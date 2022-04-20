package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.authentication.domain.repository.IAuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class VerifyOTPUseCaseTest {

    private val repository: IAuthRepository = mockk()
    private val verifyOTPUseCase = VerifyOTPUseCase(repository)

    @Test
    fun `on invoke repository creates account`(): Unit = runBlocking {
        coEvery { repository.verifyOTP(any()) }.coAnswers { mockk() }
        val request: VerifyOTPRequest = mockk()
        verifyOTPUseCase(request)
        coVerify { repository.verifyOTP(request) }
    }
}