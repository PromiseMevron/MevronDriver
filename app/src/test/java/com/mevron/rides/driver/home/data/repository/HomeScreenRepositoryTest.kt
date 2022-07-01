package com.mevron.rides.driver.home.data.repository

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.data.model.home.*
import com.mevron.rides.driver.home.data.network.HomeScreenApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class HomeScreenRepositoryTest {

    private val api: HomeScreenApi = mockk()
    private val repository = HomeScreenRepository(api)

    @Test
    fun `when repository#toggleStatus is invoked with success - api toggles status`(): Unit =
        runBlocking {
            val response: Response<GeneralResponse> = mockk {
                every { isSuccessful }.returns(true)
            }
            coEvery { api.toggleStatus() }.coAnswers { response }
            val result = repository.toggleStatus()
            coVerify { api.toggleStatus() }
            assertTrue(result is DomainModel.Success)
        }

    @Test
    fun `when repository#toggleStatus is invoked with error - returns error`(): Unit = runBlocking {
        val response: Response<GeneralResponse> = mockk {
            every { isSuccessful }.returns(false)
            every { errorBody() }.returns(mockk())
        }
        coEvery { api.toggleStatus() }.coAnswers { response }
        val result = repository.toggleStatus()
        coVerify { api.toggleStatus() }
        assertTrue(result is DomainModel.Error)
    }

    @Test
    fun `when repository#getDocumentStatus is successful - returns document`(): Unit = runBlocking {
        val response: Response<HomeScreenDataResponse> = mockk {
            every { isSuccessful }.returns(true)
            every { body() }.returns(
                HomeScreenDataResponse(
                    successData = SuccessData(
                        contentData = emptyHomeScreenData(),
                        status = "SUCCESS"
                    )
                )
            )
        }
        coEvery { api.getHomeStatus() }.returns(response)
        val result = repository.getDocumentStatus()
        coVerify { api.getHomeStatus() }
        assertTrue(result is DomainModel.Success)
    }

    @Test
    fun `when repository#getDocumentStatus fails - returns error`(): Unit = runBlocking {
        val response: Response<HomeScreenDataResponse> = mockk {
            every { isSuccessful }.returns(false)
            every { errorBody() }.returns(mockk())
        }
        coEvery { api.getHomeStatus() }.returns(response)
        val result = repository.getDocumentStatus()
        coVerify { api.getHomeStatus() }
        assertTrue(result is DomainModel.Error)
    }

    private fun emptyHomeScreenData() = HomeScreenContentData(
        documentStatus = 0,
        earnings = Earnings(
            balance = "",
            currency = "",
            nextPaymentDate = "",
            earningGoal = EarningGoal(
                earned_goal = "",
                expiryDate = "",
                percentage = "",
                weeklyGoal = ""
            ),
            todayActivity = TodayActivityX(
                earnings = "",
                online = "",
                rides = 0,
                trip_list = mutableListOf()
            ),
            weeklySummary = WeeklySummary(
                earnings = "",
                online = "",
                rides = 0,
                tripList = mutableListOf()
            )
        ),
        onlineStatus = false,
        drive = Drive(
            mutableListOf(),
            mutableListOf(),
            0,
            todayActivity = TodayActivity(earnings = "", online = "", rides = 0),
            weeklyChallenges = mutableListOf()
        )

    )
}
