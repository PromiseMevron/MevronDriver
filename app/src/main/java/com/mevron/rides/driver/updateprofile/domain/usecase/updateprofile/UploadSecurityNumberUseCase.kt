package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.model.SecurityNumRequest
import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import javax.inject.Inject

class UploadSecurityNumberUseCase @Inject constructor(
    private val repository: IUpdateProfileRepository
) {
    suspend operator fun invoke(request: SecurityNumRequest) =
        repository.uploadSecurityNumber(request)
}