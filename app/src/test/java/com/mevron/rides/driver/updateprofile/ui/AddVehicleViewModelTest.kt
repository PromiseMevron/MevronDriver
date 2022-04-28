package com.mevron.rides.driver.updateprofile.ui

import app.cash.turbine.test
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.updateprofile.domain.model.CarMakesDomainData
import com.mevron.rides.driver.updateprofile.domain.model.CarModelDomainData
import com.mevron.rides.driver.updateprofile.domain.model.CarYearDomainData
import com.mevron.rides.driver.updateprofile.domain.usecase.carproperties.GetCarMakesUseCase
import com.mevron.rides.driver.updateprofile.domain.usecase.carproperties.GetCarModelsUseCase
import com.mevron.rides.driver.updateprofile.domain.usecase.carproperties.GetCarYearsUseCase
import com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile.AddVehicleUseCase
import com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile.AddVehicleUseCaseAggregate
import com.mevron.rides.driver.updateprofile.ui.event.AddVehicleEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

/**
 * We are currently testing state change ond event received, we need to deep test data
 * change as well. Due to time constraint, we will suspend deep test to another iteration
 * TODO Complete the test for [AddVehicleViewModel]
 */
class AddVehicleViewModelTest {

    private val getCarMakesUseCaseMock: GetCarMakesUseCase = mockk()
    private val getCarModelsUseCaseMock: GetCarModelsUseCase = mockk()
    private val getCarYearsUseCaseMock: GetCarYearsUseCase = mockk()
    private val addVehicleUseCaseMock: AddVehicleUseCase = mockk()

    private val addVehicleUseCaseAggregate: AddVehicleUseCaseAggregate = mockk()

    private val viewModel = AddVehicleViewModel(addVehicleUseCaseAggregate)

    private val testDispatcher = TestCoroutineDispatcher()

    private fun setUpMocks() {

        coEvery { getCarMakesUseCaseMock() }.coAnswers {
            DomainModel.Success(
                data = CarMakesDomainData(
                    listOf()
                )
            )
        }
        coEvery { getCarModelsUseCaseMock(any()) }.coAnswers {
            DomainModel.Success(
                data = CarModelDomainData(
                    listOf()
                )
            )
        }
        coEvery { getCarYearsUseCaseMock(any()) }.coAnswers {
            DomainModel.Success(
                data = CarYearDomainData(
                    listOf()
                )
            )
        }
        coEvery { addVehicleUseCaseMock(any()) }.coAnswers {
            DomainModel.Success(
                data = DomainModel.Success(
                    data = Unit
                )
            )
        }

        every { addVehicleUseCaseAggregate.getCarMakesUseCase }.returns(getCarMakesUseCaseMock)
        every { addVehicleUseCaseAggregate.getCarModelsUseCase }.returns(getCarModelsUseCaseMock)
        every { addVehicleUseCaseAggregate.getCarYearsUseCase }.returns(getCarYearsUseCaseMock)
        every { addVehicleUseCaseAggregate.addVehicleUseCase }.returns(addVehicleUseCaseMock)
    }

    @Test
    fun `when viewModel receives event - viewModel handles event`() {
        viewModel.onEventReceived(AddVehicleEvent.AddVehicleClicked)
        coVerify { addVehicleUseCaseMock(any()) }
        viewModel.onEventReceived(AddVehicleEvent.GetCarMakesClicked)
        coVerify { getCarMakesUseCaseMock() }
        viewModel.updateSelectedData(selectedCarMake = "Make")
        viewModel.onEventReceived(AddVehicleEvent.GetCarModelsClicked)
        coVerify { getCarModelsUseCaseMock(any()) }
        viewModel.updateSelectedData(selectedCarModel = "Model")
        viewModel.onEventReceived(AddVehicleEvent.GetCarYearsClicked)
        coVerify { getCarYearsUseCaseMock(any()) }
    }

    @Test
    fun `when viewModel update sate is invoked - viewModel update state`(): Unit = runBlocking {
        viewModel.updateState(isLoading = true)
        viewModel.state.test {
            assert(awaitItem().isLoading)
        }
    }

    @Before
    fun setup() {
        setUpMocks()
        Dispatchers.setMain(testDispatcher)
    }
}


