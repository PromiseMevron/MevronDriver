package com.mevron.rides.driver.updateprofile.ui

import app.cash.turbine.test
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile.UploadSecurityNumberUseCase
import com.mevron.rides.driver.updateprofile.ui.event.AddSocialSecurityNumberError
import com.mevron.rides.driver.updateprofile.ui.event.AddSocialSecurityNumberEvent
import com.mevron.rides.driver.updateprofile.ui.event.AddSocialSecurityNumberState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SocialSecurityViewModelV2Test {

    private val useCase: UploadSecurityNumberUseCase = mockk()
    private val viewModel = SocialSecurityViewModel(useCase)

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `when viewModel#onEventReceived is BackButtonClick - ViewModel handles event`() =
        runBlocking {
            viewModel.onEventReceived(AddSocialSecurityNumberEvent.BackButtonClick)
            viewModel.state.test {
                assert(awaitItem() == AddSocialSecurityNumberState.DEFAULT.copy(shouldRouteBack = true))
            }
        }

    @Test
    fun `when viewModel#onEventReceived is AddSocialSecurityNumberButtonClick - ViewModel handles event`(): Unit =
        runBlocking {
            coEvery { useCase(any()) }.coAnswers {
                DomainModel.Success(data = Unit)
            }

            viewModel.onEventReceived(AddSocialSecurityNumberEvent.AddSocialSecurityNumberButtonClick)

            viewModel.state.test {
                val current = awaitItem()
                assertFalse(current.isLoading)
                assertTrue(current.isRequestSuccess)
            }
        }

    @Test
    fun `when viewModel#onEventReceived is AddSocialSecurityNumberButtonClick with error - ViewModel handles event`(): Unit =
        runBlocking {
            coEvery { useCase(any()) }.coAnswers {
                DomainModel.Error(error = Throwable("Failed"))
            }

            viewModel.onEventReceived(AddSocialSecurityNumberEvent.AddSocialSecurityNumberButtonClick)

            viewModel.state.test {
                val current = awaitItem()
                assertFalse(current.isLoading)
                assertFalse(current.isRequestSuccess)
                assertEquals(
                    AddSocialSecurityNumberError.RequestError(message = "Failed"),
                    current.error
                )
            }
        }
}