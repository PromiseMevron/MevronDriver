package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import javax.inject.Inject
import okhttp3.MultipartBody

class UploadStickerUseCase @Inject constructor(private val repository: IUpdateProfileRepository){

    suspend operator fun invoke(image: MultipartBody.Part) = repository.uploadSticker(image)
}