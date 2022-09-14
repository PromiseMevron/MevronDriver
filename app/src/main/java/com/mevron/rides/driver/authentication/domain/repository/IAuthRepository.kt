package com.mevron.rides.driver.authentication.domain.repository

import com.mevron.rides.driver.authentication.data.models.createaccount.GetCityRequest
import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.authentication.domain.model.RegisterPhoneRequest
import com.mevron.rides.driver.domain.DomainModel

interface IAuthRepository {
    suspend fun registerPhone(registerPhoneRequest: RegisterPhoneRequest): DomainModel
    suspend fun verifyOTP(verifyOTPRequest: VerifyOTPRequest): DomainModel
    suspend fun createAccount(createAccountRequest: CreateAccountRequest): DomainModel
    suspend fun getCities(getCityRequest: GetCityRequest): DomainModel
}