package com.mevron.rides.driver.home.data.repository

import app.cash.turbine.test
import com.mevron.rides.driver.home.domain.ISurgeRepository
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SurgeRepositoryTest {

    private val api: ISurgeRepository = mockk(relaxed = true)
    private val repository = SurgeRepository()

    @Test
    fun `when data is set in repository - data can be observed`(): Unit = runBlocking {
        repository.setSurgeUrl("testUrl")

        repository.surgeUrl.test {
            assertEquals(awaitItem(), "testUrl")
        }
    }
}