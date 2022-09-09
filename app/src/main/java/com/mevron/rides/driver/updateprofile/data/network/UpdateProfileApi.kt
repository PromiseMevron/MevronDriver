package com.mevron.rides.driver.updateprofile.data.network

import com.mevron.rides.driver.data.model.DefaultResponse
import com.mevron.rides.driver.sidemenu.vehicle.data.model.addvehicle.AddVehicleResponse
import com.mevron.rides.driver.updateprofile.domain.model.AddVehicleRequest
import com.mevron.rides.driver.updateprofile.domain.model.SecurityNumRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UpdateProfileApi {

    @POST("api/v1/driver/auth/vehicle/create")
    suspend fun addVehicle(@Body data: AddVehicleRequest): Response<AddVehicleResponse>

    @Multipart
    @POST("api/v1/driver/auth/upload/driver-license")
    suspend fun uploadLicence(@Part image: MultipartBody.Part): Response<DefaultResponse>

    @Multipart
    @POST("api/v1/driver/auth/upload/vehicle-registration-sticker")
    suspend fun uploadSticker(@Part image: MultipartBody.Part): Response<DefaultResponse>

    @Multipart
    @POST("api/v1/driver/auth/upload/profile-photo")
    suspend fun uploadProfile(@Part image: MultipartBody.Part): Response<DefaultResponse>

    @Multipart
    @POST("api/v1/driver/auth/upload/vehicle-insurance")
    suspend fun uploadInsurance(@Part image: MultipartBody.Part): Response<DefaultResponse>

    @POST("api/v1/driver/auth/update/social-security-number")
    suspend fun uploadSecurityNumber(@Body data: SecurityNumRequest): Response<DefaultResponse>
}