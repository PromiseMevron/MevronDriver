package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.repository.IAuthRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(private val repository: IAuthRepository) {

    suspend operator fun invoke(createAccountRequest: CreateAccountRequest) =
        repository.createAccount(createAccountRequest)
}