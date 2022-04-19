package com.mevron.rides.driver.authentication.data.repository

import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.authentication.data.models.createaccount.CreateAccountResponse
import com.mevron.rides.driver.authentication.domain.model.RegisterPhoneRequest
import com.mevron.rides.driver.authentication.data.models.registerphone.RegisterPhoneResponse
import com.mevron.rides.driver.authentication.data.models.validateotprequest.VerifyOTPResponse
import com.mevron.rides.driver.authentication.data.network.AuthApi
import com.mevron.rides.driver.authentication.domain.model.CreateAccountDomainModel
import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.model.RegisterPhoneDomainData
import com.mevron.rides.driver.authentication.domain.model.VerifyOTPDomainModel
import com.mevron.rides.driver.authentication.domain.repository.IAuthRepository
import com.mevron.rides.driver.data.network.collect
import com.mevron.rides.driver.domain.DomainModel

class AuthRepository(private val authApi: AuthApi) : IAuthRepository {

    override suspend fun registerPhone(registerPhoneRequest: RegisterPhoneRequest): DomainModel =
        authApi.registerPhone(registerPhoneRequest).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Empty result found"))
            } else {
                DomainModel.Error(Throwable(it.errorBody().toString()))
            }
        }

    override suspend fun verifyOTP(verifyOTPRequest: VerifyOTPRequest): DomainModel =
        authApi.verifyOTP(verifyOTPRequest).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Empty result found"))
            } else {
                DomainModel.Error(Throwable(it.errorBody().toString()))
            }
        }

    override suspend fun createAccount(createAccountRequest: CreateAccountRequest): DomainModel =
        authApi.createAccount(createAccountRequest).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Empty result found"))
            } else {
                DomainModel.Error(Throwable(it.errorBody().toString()))
            }
        }

    private fun CreateAccountResponse.toDomainModel() = DomainModel.Success(
        data = CreateAccountDomainModel(
            message = this.message,
            status = this.status
        )
    )

    private fun VerifyOTPResponse.toDomainModel() = DomainModel.Success(
        data = VerifyOTPDomainModel(
            accessToken = this.otpSuccess.otpData.accessToken,
            riderType = this.otpSuccess.otpData.riderType,
            type = this.otpSuccess.otpData.type,
            uuid = this.otpSuccess.otpData.uuid
        )
    )

    private fun RegisterPhoneResponse.toDomainModel() =
        DomainModel.Success(
            data = RegisterPhoneDomainData(
                phoneCodeData = this.successData.phoneCodeData,
                message = this.successData.message,
                status = this.successData.status
            )
        )
}