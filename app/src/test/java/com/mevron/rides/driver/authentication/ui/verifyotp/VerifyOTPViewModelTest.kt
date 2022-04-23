package com.mevron.rides.driver.authentication.ui.verifyotp

import app.cash.turbine.test
import com.mevron.rides.driver.authentication.domain.usecase.VerifyOTPUseCase
import com.mevron.rides.driver.authentication.ui.verifyotp.event.VerifyOTPEvent
import com.mevron.rides.driver.authentication.ui.verifyotp.state.VerifyOTPState
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
class VerifyOTPViewModelTest {

    private val useCase: VerifyOTPUseCase = mockk()
    private val viewModel = VerifyOTPViewModel(useCase)


    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        coEvery { useCase(any()) }.coAnswers { mockk() }
        Dispatchers.setMain(testDispatcher)
    }


    @Test
    fun `when VeriftOTPViewModel#updateState is invoked - state is updated`() = runBlocking {
        viewModel.updateState(isLoading = true, code = "code")
        viewModel.state.test {
            assertEquals(
                VerifyOTPState.EMPTY.copy(isLoading = true, code = "code"),
                awaitItem()
            )
        }
    }

    @Test
    fun `when VerifyOTPViewModel#onEvent is invoked for OTPComplete  - ViewModel process event`() {
        viewModel.onEvent(VerifyOTPEvent.OnOTPComplete)
        coVerify { useCase(any()) }
    }
}