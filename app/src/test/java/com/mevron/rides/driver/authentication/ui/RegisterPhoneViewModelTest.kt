package com.mevron.rides.driver.authentication.rig.ui

import app.cash.turbine.test
import com.mevron.rides.driver.authentication.rig.domain.usecase.RegisterPhoneUseCase
import com.mevron.rides.driver.authentication.rig.ui.event.RegisterPhoneEvent
import com.mevron.rides.driver.authentication.rig.ui.state.RegisterPhoneState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RegisterPhoneViewModelTest {

    private val useCase: RegisterPhoneUseCase = mockk()
    private val viewModel = RegisterPhoneViewModel(useCase)

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        coEvery { useCase(any()) }.coAnswers { mockk() }
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `when RegisterPhoneViewModel#updateState is invoked - state is updated`() = runBlocking {
        viewModel.updateState(loading = true, countryCode = "code")
        viewModel.state.test {
            assertEquals(RegisterPhoneState.empty().copy(loading = true, countryCode = "code"), awaitItem())
        }
    }

    @Test
    fun `when RegisterPhoneViewModel#onEvent is invoked for RegisterPhone  - ViewModel process event`() {
        viewModel.onEvent(RegisterPhoneEvent.RegisterPhoneClick)
        coVerify { useCase(any()) }
    }
}