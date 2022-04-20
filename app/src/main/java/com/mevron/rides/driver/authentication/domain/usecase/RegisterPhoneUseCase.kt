package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.domain.model.RegisterPhoneRequest
import com.mevron.rides.driver.authentication.domain.repository.IAuthRepository
import javax.inject.Inject

class RegisterPhoneUseCase @Inject constructor(private val repository: IAuthRepository) {

    suspend operator fun invoke(registerPhoneRequest: RegisterPhoneRequest) =
        repository.registerPhone(registerPhoneRequest)
}