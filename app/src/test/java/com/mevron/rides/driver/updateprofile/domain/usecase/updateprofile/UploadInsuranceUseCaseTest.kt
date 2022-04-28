package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import org.junit.Test

class UploadInsuranceUseCaseTest {

    private val repository: IUpdateProfileRepository = mockk()
    private val useCase = UploadInsuranceUseCase(repository)

    @Test
    fun `when UploadInsuranceUseCase is invoked - uploads insurance to the repository`(): Unit =
        runBlocking {
            coEvery { repository.uploadInsurance(any()) }.coAnswers { mockk() }
            val request: MultipartBody.Part = mockk()
            useCase(request)
            coVerify { repository.uploadInsurance(request) }
        }
}