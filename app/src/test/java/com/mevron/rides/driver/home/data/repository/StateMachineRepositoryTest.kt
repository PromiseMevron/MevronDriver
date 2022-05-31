package com.mevron.rides.driver.home.data.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.data.model.MetaData
import com.mevron.rides.driver.home.data.model.StateMachineResponse
import com.mevron.rides.driver.home.data.network.StateMachineApi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class StateMachineRepositoryTest {

    private val api: StateMachineApi = mockk(relaxed = true)
    private val repository = StateMachineRepository(api)

    @Test
    fun `when repository is invoked - calls api to get current state`(): Unit = runBlocking {
        val response: Response<StateMachineResponse> = mockk {
            every { isSuccessful }.returns(true)
            every { body() }.returns(StateMachineResponse("ORDER", MetaData("", "")))
        }
        coEvery { api.getStateMachine() }.coAnswers { response }
        val result = repository.getStateMachineState()
        assertTrue(result is DomainModel.Success)
    }

    @Test
    fun `when repository is invoked with error - returns error`(): Unit = runBlocking {
        val response: Response<StateMachineResponse> = mockk {
            every { isSuccessful }.returns(false)
            every { errorBody() }.returns(mockk())
        }
        coEvery { api.getStateMachine() }.coAnswers { response }
        val result = repository.getStateMachineState()
        assertTrue(result is DomainModel.Error)
    }
}