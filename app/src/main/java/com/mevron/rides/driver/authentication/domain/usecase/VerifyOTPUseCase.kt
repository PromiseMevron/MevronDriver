package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.authentication.domain.repository.IAuthRepository
import javax.inject.Inject

class VerifyOTPUseCase @Inject constructor(private val repository: IAuthRepository) {

    suspend operator fun invoke(verifyOTPRequest: VerifyOTPRequest) =
        repository.verifyOTP(verifyOTPRequest)
}

