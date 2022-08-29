package com.mevron.rides.driver.cashout.data.network

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.cashout.data.model.*
import com.mevron.rides.driver.cashout.data.model.banklist.BnakListResponse
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.remote.model.getcard.GetCardResponse
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.payment.PaymentMethodResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface PaymentAPI {

    @GET("api/v1/driver/auth/wallet/details")
    suspend fun getWalletDetails(): Response<PaymentDetailsResponse>

    @POST("api/v1/driver/auth/wallet/cashOut")
    suspend fun cashOut(@Body data: CashActionData): Response<GeneralResponse>

    @GET("api/v1/driver/auth/bank/specification")
    suspend fun bankSpecification(): Response<GetBankSpecifications>

    @POST("api/v1/driver/auth/bank/create")
    suspend fun addBank(@Body data : AddBankAccountSpecification): Response<GeneralResponse>

    @POST("api/v1/driver/auth/wallet/addFunds")
    suspend fun addFund(@Body data : CashActionData): Response<GeneralResponse>

    @GET("api/v1/driver/auth/payment-method")
    suspend fun getCards(): Response<PaymentMethodResponse>

    @POST("api/v1/driver/auth/payment-method/payment-link")
    suspend fun getPaymentLink(@Body data: GetLinkAmount): Response<PaymentLink>

    @GET
    suspend fun confirmPayment(@Url theUrl: String): Response<GeneralResponse>

    @POST("api/v1/driver/auth/bank-account/create")
    suspend fun addNigBanks(@Body data: AddAccountPayload): Response<GeneralResponse>

    @POST("api/v1/driver/auth/bank/resolve-account")
    suspend fun resolveASccountNumber(@Body data: AddAccountPayload): Response<ResolveAccount>

    @GET("api/v1/driver/auth/bank/list")
    suspend fun getBankList(): Response<BnakListResponse>
}
