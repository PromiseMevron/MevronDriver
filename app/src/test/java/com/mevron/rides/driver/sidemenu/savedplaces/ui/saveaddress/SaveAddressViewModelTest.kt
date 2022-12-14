package com.mevron.rides.driver.sidemenu.savedplaces.ui.saveaddress

import app.cash.turbine.test
import com.mevron.rides.driver.sidemenu.savedplaces.domain.usecases.SaveAddressUseCase
import com.mevron.rides.driver.sidemenu.savedplaces.ui.saveaddress.event.SaveAddressEvent
import com.mevron.rides.driver.sidemenu.savedplaces.ui.saveaddress.state.SaveAddressState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SaveAddressViewModelTest {
    val useCase = mockk<SaveAddressUseCase>()
    val viewModel = SaveAddressViewModel(useCase)
    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        coEvery { useCase(any()) }.coAnswers { mockk() }
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `whenSaveAddressDetailViewModel#updateState is invoked - state is updated`() = runBlocking {
        viewModel.updateState(isLoading = true, backPressed = true)
        viewModel.state.test {
            assertEquals(
                SaveAddressState.EMPTY.copy(isLoading = true, backPressed = true),
                awaitItem()
            )
        }
    }

    @Test
    fun `when SaveAddressDetailViewModel#onEvent#AddNew is invoked for OnButtonClick  - ViewModel process event`() {
        viewModel.handleEvent(SaveAddressEvent.SaveHomeWorkAddress)
        coVerify { useCase(any()) }
    }

    @Test
    fun `when SaveAddressDetailViewModel#onEvent#backbutton is invoked for OnButtonClick  - ViewModel process event`() =
        runBlocking {
            viewModel.handleEvent(SaveAddressEvent.OnBackButtonPressed)
            viewModel.state.test {
                assertEquals(
                    SaveAddressState.EMPTY.copy(backPressed = true),
                    awaitItem()
                )
            }
        }

    @Test
    fun `when SaveAddressDetailViewModel#onEvent#save other address event is invoked for OnButtonClick  - ViewModel process event`() =
        runBlocking {
            viewModel.handleEvent(SaveAddressEvent.SaveOtherAddress)
            viewModel.state.test {
                assertEquals(
                    SaveAddressState.EMPTY.copy(openNextPage = true),
                    awaitItem()
                )
            }
        }

    @Test
    fun `when SaveAddressDetailViewModel#onEvent#address selected is invoked for OnButtonClick  - ViewModel process event`() =
        runBlocking {
            viewModel.handleEvent(SaveAddressEvent.AddressSelected)
            viewModel.state.test {
                assertEquals(
                    SaveAddressState.EMPTY.copy(addressSClickedOn = true),
                    awaitItem()
                )
            }
        }

    @Test
    fun `when SaveAddressDetailViewModel#onEvent#query test change is invoked for OnButtonClick  - ViewModel process event`() =
        runBlocking {
            viewModel.handleEvent(SaveAddressEvent.OnTextChanged)
            viewModel.state.test {
                assertEquals(
                    SaveAddressState.EMPTY.copy(queryChanged = true),
                    awaitItem()
                )
            }
        }

}