package com.mevron.rides.driver.authentication.domain.usecase

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.mevron.rides.driver.authentication.data.repository.PreferenceRepository
import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetPreferenceUseCaseTest{
    private lateinit var repository: IPreferenceRepository
    private lateinit var getPreferenceUseCase: GetPreferenceUseCase


    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        repository = PreferenceRepository(context)
        getPreferenceUseCase = GetPreferenceUseCase(repository)
    }

    @Test
    fun on_invoke_repository_get_a_saved_string(): Unit = runBlocking {


    }
}