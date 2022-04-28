package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import org.junit.Test

class UploadLicenceUseCaseTest {

    private val repository: IUpdateProfileRepository = mockk()
    private val useCase = UploadLicenceUseCase(repository)

    @Test
    fun `when UploadLicenceUseCase is invoked - repository uploads insurance`(): Unit =
        runBlocking {
            coEvery { repository.uploadLicence(any()) }.coAnswers { mockk() }
            val request: MultipartBody.Part = mockk()
            useCase(request)
            coVerify { repository.uploadLicence(request) }
        }
}