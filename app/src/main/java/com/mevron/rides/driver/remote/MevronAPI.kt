package com.mevron.rides.driver.remote

import com.mevron.rides.driver.auth.model.*
import com.mevron.rides.driver.auth.model.caryear.GetCarYear
import com.mevron.rides.driver.auth.model.getcar.GetCallsResponse
import com.mevron.rides.driver.auth.model.getmodel.GetModelResponse
import com.mevron.rides.driver.home.model.HomeScreenResponse
import com.mevron.rides.driver.home.model.documents.DocumentStatusResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MevronAPI {

    @POST("api/v1/driver/request-otp")
    suspend fun registerPhone(@Body data: RegisterBody): Response<RegisterResponse>

    @POST("api/v1/driver/validate-otp")
    suspend fun verifyOTP(@Body data: ValidateOTPRequest): Response<OTPResponse>

    @POST("api/v1/driver/auth/update-account")
    suspend fun createAccount(@Body data: CreateAccountRequest): Response<GeneralResponse>

    @POST("api/v1/driver/auth/vehicle/create")
    suspend fun addVehicle(@Body data: VehicleAddRequest): Response<GeneralResponse>

    @Multipart
    @POST("api/v1/driver/auth/upload/driver-license")
    suspend fun uploadLicence(@Part image: MultipartBody.Part): Response<GeneralResponse>

    @Multipart
    @POST("api/v1/driver/auth/upload/vehicle-registration-sticker")
    suspend fun uploadSticker(@Part image: MultipartBody.Part): Response<GeneralResponse>

    @Multipart
    @POST("api/v1/driver/auth/upload/profile-photo")
    suspend fun uploadProfile(@Part image: MultipartBody.Part): Response<GeneralResponse>

    @Multipart
    @POST("api/v1/driver/auth/upload/vehicle-insurance")
    suspend fun uploadInsurance(@Part image: MultipartBody.Part): Response<GeneralResponse>

    @POST("api/v1/driver/auth/update/social-security-number")
    suspend fun uploadSecurityNumber(@Body data: SecurityNumRequest): Response<GeneralResponse>


    @GET("api/v1/driver/auth/get-car-list")
    suspend fun getCarMakes(): Response<GetCallsResponse>

    @GET("api/v1/driver/auth/get-car-model")
    suspend fun getCarModels(@QueryMap params: HashMap<String, String>): Response<GetModelResponse>

    @GET("api/v1/driver/auth/get-car-year")
    suspend fun getCarYear(@QueryMap params: HashMap<String, String>): Response<GetCarYear>

    @POST("api/v1/driver/auth/trip")
    suspend fun tripManagement(@Body data: TripManagementModel): Response<GeneralResponse>

    @POST("api/v1/driver/auth/toggle-online-status")
    suspend fun toggleStatus(): Response<GeneralResponse>

    @GET("api/v1/driver/auth/homescreen")
    suspend fun getHomeStatus(): Response<HomeScreenResponse>

    @POST("api/v1/driver/auth/document-status")
    suspend fun getDocumentStatus(): Response<DocumentStatusResponse>



}