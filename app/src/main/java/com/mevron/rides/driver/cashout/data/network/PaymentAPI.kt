package com.mevron.rides.driver.cashout.data.network

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.cashout.data.model.AddBankAccountSpecification
import com.mevron.rides.driver.cashout.data.model.CashActionData
import com.mevron.rides.driver.cashout.data.model.GetBankSpecifications
import com.mevron.rides.driver.cashout.data.model.PaymentDetailsResponse
import com.mevron.rides.driver.remote.model.getcard.GetCardResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentAPI {

    @GET("api/v1/driver/auth/wallet/details")
    suspend fun getWalletDetails(): Response<PaymentDetailsResponse>

    @POST("api/v1/driver/auth/wallet/cashOut")
    suspend fun cashOut(@Body data: CashActionData): Response<GeneralResponse>

    @GET("api/v1/driver/auth/bank/specification")
    suspend fun bankSpecification(): Response<GetBankSpecifications>

    @POST("api/v1/driver/auth/bank/create")
    suspend fun addBank(@Body data : AddBankAccountSpecification): Response<GeneralResponse>

    @POST("api/v1/driver/auth/bank/create")
    suspend fun addFund(@Body data : CashActionData): Response<GeneralResponse>

    @GET("api/v1/driver/auth/payment-method")
    suspend fun getCards(): Response<GetCardResponse>
}
