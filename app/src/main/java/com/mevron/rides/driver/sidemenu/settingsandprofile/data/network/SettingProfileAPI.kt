package com.mevron.rides.driver.sidemenu.settingsandprofile.data.network

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SettingProfileAPI {

    @GET("api/v1/driver/auth/profile")
    suspend fun getProfile(): Response<GetProfileResponse>

    @POST("api/v1/driver/auth/profile")
    suspend fun updateTheProfile(@Body data: SaveDetailsRequest): Response<GeneralResponse>

    @POST("api/v1/driver/auth/resend-verification")
    suspend fun resendEmailLink(): Response<GeneralResponse>

    @POST("api/v1/driver/auth/sign-out")
    suspend fun signOut(): Response<GeneralResponse>

    @GET("api/v1/driver/auth/referral-history")
    suspend fun getReferralHistory(): Response<GetReferalHistory>

    @POST("api/v1/driver/auth/referral-history")
    suspend fun setReferral(data: SetReferal): Response<GeneralResponse>

    @POST("api/v1/driver/auth/referral-report")
    suspend fun getReferralReport(data: ReferalReport): Response<GetReferalDetail>

}