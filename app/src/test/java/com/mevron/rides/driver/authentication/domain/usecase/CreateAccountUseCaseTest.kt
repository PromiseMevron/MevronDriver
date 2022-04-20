package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.repository.IAuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateAccountUseCaseTest {

    private val repository: IAuthRepository = mockk()
    private val createAccountUseCase = CreateAccountUseCase(repository)

    @Test
    fun `on invoke repository creates account`(): Unit = runBlocking {
        coEvery { repository.createAccount(any()) }.coAnswers { mockk() }
        val request: CreateAccountRequest = mockk()
        createAccountUseCase(request)
        coVerify { repository.createAccount(request) }
    }
}