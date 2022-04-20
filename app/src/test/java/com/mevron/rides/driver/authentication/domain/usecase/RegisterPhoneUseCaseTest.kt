package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.domain.model.RegisterPhoneRequest
import com.mevron.rides.driver.authentication.domain.repository.IAuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RegisterPhoneUseCaseTest {

    private val repository: IAuthRepository = mockk()
    private val registerPhoneUseCase = RegisterPhoneUseCase(repository)

    @Test
    fun `on invoke repository creates account`(): Unit = runBlocking {
        coEvery { repository.registerPhone(any()) }.coAnswers { mockk() }
        val request: RegisterPhoneRequest = mockk()
        registerPhoneUseCase(request)
        coVerify { repository.registerPhone(request) }
    }
}