package com.mevron.rides.driver.remote

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.auth.model.OTPResponse
import com.mevron.rides.driver.auth.model.RegisterBody
import com.mevron.rides.driver.auth.model.RegisterResponse
import com.mevron.rides.driver.auth.model.caryear.GetCarYear
import com.mevron.rides.driver.auth.model.getcar.GetCallsResponse
import com.mevron.rides.driver.auth.model.getmodel.GetModelResponse
import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.home.model.HomeScreenResponse
import com.mevron.rides.driver.home.model.documents.DocumentStatusResponse
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.GetSavedAddress
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.SaveAddressRequest
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.UpdateAddress
import com.mevron.rides.driver.remote.model.getcard.AddCard
import com.mevron.rides.driver.remote.model.getcard.GetCardResponse
import com.mevron.rides.driver.sidemenu.emerg.data.model.AddContactRequest
import com.mevron.rides.driver.sidemenu.emerg.data.model.GetContactsResponse
import com.mevron.rides.driver.sidemenu.supportpages.data.model.NotificationResponse
import com.mevron.rides.driver.sidemenu.emerg.data.model.UpdateEmergencyContact
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.GetPrefrenceModel
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.PrefrenceData

import com.mevron.rides.driver.updateprofile.domain.model.AddVehicleRequest
import com.mevron.rides.driver.updateprofile.domain.model.SecurityNumRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface MevronAPI {

    @POST("api/v1/driver/request-otp")
    suspend fun registerPhone(@Body data: RegisterBody): Response<RegisterResponse>

    @POST("api/v1/driver/validate-otp")
    suspend fun verifyOTP(@Body data: VerifyOTPRequest): Response<OTPResponse>

    @POST("api/v1/driver/auth/update-account")
    suspend fun createAccount(@Body data: CreateAccountRequest): Response<GeneralResponse>

    @POST("api/v1/driver/auth/vehicle/create")
    suspend fun addVehicle(@Body data: AddVehicleRequest): Response<GeneralResponse>

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

    @POST("api/v1/trip/driver/auth/trip")
    suspend fun tripManagement(@Body data: TripManagementModel): Response<GeneralResponse>

    @POST("api/v1/driver/auth/toggle-online-status")
    suspend fun toggleStatus(): Response<GeneralResponse>

    @GET("api/v1/driver/auth/homescreen")
    suspend fun getHomeStatus(): Response<HomeScreenResponse>

    @POST("api/v1/driver/auth/document-status")
    suspend fun getDocumentStatus(): Response<DocumentStatusResponse>


    @POST("api/v1/driver/auth/savedPlaces")
    suspend fun saveAddress(@Body data: SaveAddressRequest):Response<GeneralResponse>

    @GET("api/v1/driver/auth/savedPlaces")
    suspend fun getAddress():Response<GetSavedAddress>

    @POST("api/v1/driver/auth/savedPlaces/{uiid}")
    suspend fun updateAddress(@Path("uiid") identifier: String, @Body data: UpdateAddress): Response<GeneralResponse>

    @POST("api/v1/driver/auth/emergency-contacts")
    suspend fun saveEmergency(@Body data: AddContactRequest): Response<GeneralResponse>

    @GET("api/v1/driver/auth/emergency-contacts")
    suspend fun getEmergency(): Response<GetContactsResponse>

    @POST("api/v1/driver/auth/emergency-contacts/{uiid}")
    suspend fun updateEmergency(@Path("uiid") id: String, data: UpdateEmergencyContact): Response<GeneralResponse>


    @DELETE("api/v1/driver/auth/emergency-contacts/{uiid}")
    suspend fun deleteEmergency(@Path("uiid") id: String): Response<GeneralResponse>

    @GET("/api/v1/driver/auth/promo-code")
    suspend fun getPromo(): Response<GeneralResponse>

    @GET("/api/v1/driver/auth/notifications?page=1")
    suspend fun getNotifications(): Response<NotificationResponse>


    @GET("api/v1/driver/auth/vehicles")
    suspend fun getAllVehicles(): Response<GeneralResponse>

    @GET("api/v1/driver/auth/vehicle/view/{uiid}")
    suspend fun getAVehicles(@Path("uiid") id: String): Response<GeneralResponse>


    @DELETE("/api/v1/driver/auth/vehicle/remove/{uiid}")
    suspend fun deleteVehicles(@Path("uiid") id: String): Response<GeneralResponse>


    @GET("api/v1/driver/auth/payment-method")
    suspend fun getCards(): Response<GetCardResponse>

    @DELETE("api/v1/driver/auth/payment-method/{uiid}/remove")
    suspend fun deleteCard(@Path("uiid") identifier: String): Response<GeneralResponse>


    @POST("api/v1/driver/auth/payment-method/create")
    suspend fun addCard(@Body data: AddCard): Response<GeneralResponse>

    @POST("/api/v1/driver/auth/set-preference")
    suspend fun setPreference(@Body data: PrefrenceData): Response<GeneralResponse>

    @GET("/api/v1/driver/auth/get-preference")
    suspend fun getPreference(@Query("email") email: String, @Query("token") token: String): Response<GetPrefrenceModel>


    @GET("api/v1/driver/auth/trips")
    suspend fun getAllTrips(@Query("startDate") startDate: String, @Query("endDate") endDate: String): Response<GeneralResponse>

}