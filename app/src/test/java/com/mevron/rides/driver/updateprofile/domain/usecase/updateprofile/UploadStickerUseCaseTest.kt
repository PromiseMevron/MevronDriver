package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import org.junit.Test

class UploadStickerUseCaseTest {

    private val repository: IUpdateProfileRepository = mockk()
    private val useCase = UploadStickerUseCase(repository)

    @Test
    fun `when UploadStickerUseCase is invoked - repository uploads sticker`(): Unit =
        runBlocking {
            coEvery { repository.uploadSticker(any()) }.coAnswers { mockk() }
            val request: MultipartBody.Part = mockk()
            useCase(request)
            coVerify { repository.uploadSticker(request) }
        }
}