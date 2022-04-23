package com.mevron.rides.driver.authentication.data.repository

import com.mevron.rides.driver.authentication.data.models.createaccount.CreateAccountResponse
import com.mevron.rides.driver.authentication.data.models.createaccount.CreateSuccess
import com.mevron.rides.driver.authentication.data.models.registerphone.PhoneCodeData
import com.mevron.rides.driver.authentication.data.models.registerphone.RegisterPhoneResponse
import com.mevron.rides.driver.authentication.data.models.registerphone.SuccessData
import com.mevron.rides.driver.authentication.data.models.validateotprequest.OTPData
import com.mevron.rides.driver.authentication.data.models.validateotprequest.OTPSuccess
import com.mevron.rides.driver.authentication.data.models.validateotprequest.VerifyOTPResponse
import com.mevron.rides.driver.authentication.data.network.AuthApi
import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.model.RegisterPhoneRequest
import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.mockError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class AuthRepositoryTest {

    private val authApi: AuthApi = mockk()
    private val repository = AuthRepository(authApi)

    @Test
    fun `when AuthRepository#registerPhone is invoked - sends api request to register phone`(): Unit =
        runBlocking {
            val registerPhoneResponseBody = RegisterPhoneResponse(
                successData = SuccessData(
                    phoneCodeData = PhoneCodeData(
                        code = "3333",
                        expireAt = "now"
                    ), message = "testMessage", status = "testStatus"
                )
            )
            val response: Response<RegisterPhoneResponse> =
                Response.success(registerPhoneResponseBody)
            coEvery { authApi.registerPhone(any()) }.coAnswers { response }
            val phoneRequest = RegisterPhoneRequest(country = "test", phoneNumber = "123")

            val result = repository.registerPhone(phoneRequest)

            coVerify { authApi.registerPhone(phoneRequest) }
            assert(result is DomainModel.Success)
        }

    @Test
    fun `when AuthApi#registerPhone fails - repository returns error`() = runBlocking {
        val response: Response<RegisterPhoneResponse> = mockError()
        coEvery { authApi.registerPhone(any()) }.coAnswers { response }
        val request: RegisterPhoneRequest = mockk()

        val result = repository.registerPhone(request)

        assert(result is DomainModel.Error)
    }

    @Test
    fun `when AuthRepository#verifyOTP is invoked - sends api request to verifyOTP`(): Unit =
        runBlocking {
            val response: Response<VerifyOTPResponse> = mockk {
                every { isSuccessful }.returns(true)
                every { body() }.returns(
                    VerifyOTPResponse(
                        OTPSuccess(
                            otpData = OTPData(
                                accessToken = "testToken",
                                riderType = "taxi",
                                type = "test",
                                uuid = "testUUID"
                            ),
                            message = "test",
                            status = "SUCCESS"
                        )
                    )
                )
            }

            coEvery { authApi.verifyOTP(any()) }.coAnswers { response }
            val otpRequest = VerifyOTPRequest(code = "233", phoneNumber = "123")

            repository.verifyOTP(otpRequest)

            coVerify { authApi.verifyOTP(otpRequest) }
        }

    @Test
    fun `when AuthApi#verifyOTP fails - repository returns error`(): Unit =
        runBlocking {
            val response: Response<VerifyOTPResponse> = mockError()
            coEvery { authApi.verifyOTP(any()) }.coAnswers { response }
            val request: VerifyOTPRequest = mockk()

            val result = repository.verifyOTP(request)

            assert(result is DomainModel.Error)
        }

    @Test
    fun `when AuthRepository#createAccount is invoked - sends api to createAccount`(): Unit =
        runBlocking {
            val response: Response<CreateAccountResponse> =
                Response.success(CreateAccountResponse(CreateSuccess("message", "200")))
            coEvery { authApi.createAccount(any()) }.coAnswers { response }
            val createAccountRequest = CreateAccountRequest(
                city = "testCity",
                email = "testEmail",
                firstName = "testFirstName",
                lastName = "testLastName",
                referralCode = "testCode"
            )

            val result = repository.createAccount(createAccountRequest)

            coVerify { authApi.createAccount(createAccountRequest) }
            assert(result is DomainModel.Success)
        }

    @Test
    fun `when AuthApi#createAccount fails - repository returns error`(): Unit =
        runBlocking {
            val response: Response<CreateAccountResponse> = mockError()
            coEvery { authApi.createAccount(any()) }.coAnswers { response }
            val request: CreateAccountRequest = mockk()

            val result = repository.createAccount(request)

            assert(result is DomainModel.Error)
        }
}