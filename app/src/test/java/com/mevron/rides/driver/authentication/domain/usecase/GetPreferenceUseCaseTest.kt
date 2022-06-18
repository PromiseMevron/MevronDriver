package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class GetPreferenceUseCaseTest {

    private val repository: IPreferenceRepository = mockk()
    private val useCase = GetSharedPreferenceUseCase(repository)

    @Test
    fun `when GetPreferenceUseCase is invoked - returns data from repository`() {
        every { repository.getStringForKey("KEY") }.returns("TEST")
        val result = useCase("KEY")
        assert(result == "TEST")
    }
}