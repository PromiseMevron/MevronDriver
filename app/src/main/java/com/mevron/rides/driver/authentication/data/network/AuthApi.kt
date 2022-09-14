package com.mevron.rides.driver.authentication.data.network

import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.authentication.data.models.createaccount.CreateAccountResponse
import com.mevron.rides.driver.authentication.data.models.createaccount.GetCitiesResponse
import com.mevron.rides.driver.authentication.data.models.createaccount.GetCityRequest
import com.mevron.rides.driver.authentication.domain.model.RegisterPhoneRequest
import com.mevron.rides.driver.authentication.data.models.registerphone.RegisterPhoneResponse
import com.mevron.rides.driver.authentication.data.models.validateotprequest.VerifyOTPResponse
import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/acquisation/driver/request-otp")
    suspend fun registerPhone(@Body registerPhoneRequest: RegisterPhoneRequest): Response<RegisterPhoneResponse>

    @POST("api/v1/acquisation/driver/validate-otp")
    suspend fun verifyOTP(@Body verifyOTPRequest: VerifyOTPRequest): Response<VerifyOTPResponse>

    @POST("api/v1/acquisation/driver/auth/update-profile")
    suspend fun createAccount(@Body data: CreateAccountRequest): Response<CreateAccountResponse>

    @POST("api/v1/acquisation/driver/get-city")
    suspend fun getCities(@Body data: GetCityRequest): Response<GetCitiesResponse>
}