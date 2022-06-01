package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.IStateMachineRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetStateMachineStateUseCaseTest {
    private val repository: IStateMachineRepository = mockk(relaxed = true)
    private val useCase = GetStateMachineStateUseCase(repository)

    @Test
    fun `when useCase is invoked - calls repository to get the current state`(): Unit =
        runBlocking {
            useCase.invoke()
            coVerify { repository.getStateMachineState() }
        }
}