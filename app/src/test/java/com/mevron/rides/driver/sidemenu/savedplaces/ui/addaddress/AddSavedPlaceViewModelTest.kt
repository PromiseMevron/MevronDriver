package com.mevron.rides.driver.sidemenu.savedplaces.ui.addaddress

import app.cash.turbine.test
import com.mevron.rides.driver.sidemenu.savedplaces.domain.usecases.GetAddressUseCase
import com.mevron.rides.driver.sidemenu.savedplaces.ui.addaddress.event.AddSavedAddressEvent
import com.mevron.rides.driver.sidemenu.savedplaces.ui.addaddress.state.AddSavedPlaceState
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
class AddSavedPlaceViewModelTest {

    val useCase = mockk<GetAddressUseCase>()
    val viewModel = AddSavedPlaceViewModel(useCase)

    val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `whenAddSavedPlaceViewModel#updateState is invoked - state is updated`() = runBlocking {
        viewModel.updateState(isLoading = true, backButton = true)
        viewModel.state.test {
            assertEquals(
                AddSavedPlaceState.EMPTY.copy(isLoading = true, backButton = true),
                awaitItem()
            )
        }
    }

    @Test
    fun `when AddSavedPlaceViewModel#onEvent#AddNew is invoked for OnButtonClick  - ViewModel process event`() =
        runBlocking {
            viewModel.handleEvent(AddSavedAddressEvent.OnAddNewClicked)
            viewModel.state.test {
                assertEquals(
                    AddSavedPlaceState.EMPTY.copy(openNextPage = true),
                    awaitItem()
                )
            }
        }

    @Test
    fun `when AddSavedPlaceViewModel#onEvent#backbutton is invoked for OnButtonClick  - ViewModel process event`() =
        runBlocking {
            viewModel.handleEvent(AddSavedAddressEvent.OnBackButtonClicked)
            viewModel.state.test {
                assertEquals(
                    AddSavedPlaceState.EMPTY.copy(backButton = true),
                    awaitItem()
                )
            }
        }

    @Test
    fun `when AddSavedPlaceViewModel#onEvent is invoked for OnButtonClick  - ViewModel process event`() {
        viewModel.handleEvent(AddSavedAddressEvent.GetNewAddress)
        coVerify { useCase() }
    }


}