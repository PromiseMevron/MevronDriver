package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import org.junit.Test

class UploadProfileUseCaseTest {

    private val repository: IUpdateProfileRepository = mockk()
    private val useCase = UploadProfileUseCase(repository)

    @Test
    fun `when UploadProfileUseCase is invoked - repository uploads profile`(): Unit =
        runBlocking {
            coEvery { repository.uploadProfile(any()) }.coAnswers { mockk() }
            val request: MultipartBody.Part = mockk()
            useCase(request)
            coVerify { repository.uploadProfile(request) }
        }
}