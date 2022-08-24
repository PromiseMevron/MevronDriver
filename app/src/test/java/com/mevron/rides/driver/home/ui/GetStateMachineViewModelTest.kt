package com.mevron.rides.driver.home.ui

import app.cash.turbine.test
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.domain.model.StateMachineDomainData
import com.mevron.rides.driver.home.domain.model.StateMachineMetaData
import com.mevron.rides.driver.home.domain.usecase.GetStateMachineStateUseCase
import io.mockk.coEvery
import io.mockk.mockk
import java.util.concurrent.Executors
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetStateMachineViewModelTest {

    private val useCase: GetStateMachineStateUseCase = mockk(relaxed = true)
    private val viewModel = GetStateMachineViewModel(useCase)

    private val dispatcher = Executors
        .newSingleThreadExecutor()
        .asCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.close()
    }

    @Test
    fun `when viewModel#getStateMachine is invoked - viewModel updates the current view state`(): Unit =
        runBlocking {
            launch(Dispatchers.Main) {
                coEvery { useCase.invoke() }.coAnswers {
                    DomainModel.Success(
                        data = StateMachineDomainData(
                            Pair("ORDER", StateMachineMetaData.EMPTY)
                        )
                    )
                }
                viewModel.getStateMachine()
                viewModel.stateMachineState.test {
                    assertEquals(
                        StateMachineDomainData(
                            Pair("ORDER", StateMachineMetaData.EMPTY)
                        ),
                        awaitItem().data
                    )
                }
            }
        }
}