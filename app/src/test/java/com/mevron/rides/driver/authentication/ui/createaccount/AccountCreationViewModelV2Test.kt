package com.mevron.rides.driver.authentication.ui.createaccount

import app.cash.turbine.test
import com.mevron.rides.driver.authentication.domain.usecase.CreateAccountUseCase
import com.mevron.rides.driver.authentication.domain.usecase.SetPreferenceUseCase
import com.mevron.rides.driver.authentication.ui.createaccount.event.CreateAccountEvent
import com.mevron.rides.driver.authentication.ui.createaccount.state.CreateAccountState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountCreationViewModelV2Test {

    private val useCase: CreateAccountUseCase = mockk()
    private val prefUseCase: SetPreferenceUseCase = mockk()
    private val viewModel = AccountCreationViewModelV2(useCase, prefUseCase)

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        coEvery { useCase(any()) }.coAnswers { mockk() }
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `whenAccountCreationViewModel#updateState is invoked - state is updated`() = runBlocking {
        viewModel.updateState(isLoading = true, phoneNumber = "code")
        viewModel.state.test {
            assertEquals(
                CreateAccountState.EMPTY.copy(isLoading = true, phoneNumber = "code"),
                awaitItem()
            )
        }
    }

    @Test
    fun `when AccountCreationViewModel#onEvent is invoked for OnButtonClick  - ViewModel process event`() {
        viewModel.handleEvent(CreateAccountEvent.OnCreateAccountClick)
        coVerify { useCase(any()) }
    }
}