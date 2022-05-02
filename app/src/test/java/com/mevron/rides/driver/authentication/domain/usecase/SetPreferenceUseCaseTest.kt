package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import io.mockk.*
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class SetPreferenceUseCaseTest {

    private val repository: IPreferenceRepository = mockk()
    private val useCase = SetPreferenceUseCase(repository)

    @Test
    fun `test that when the preference is called there is an interaction`(){
        every { repository.setStringForKey("KEY", "TEST") }.just(Runs)
        useCase("KEY", "TEST")
        verify { repository.setStringForKey("KEY", "TEST") }
    }
}