package com.mevron.rides.driver.remote

import com.mevron.rides.driver.auth.model.*
import com.mevron.rides.driver.auth.model.caryear.GetCarYear
import com.mevron.rides.driver.auth.model.getcar.GetCallsResponse
import com.mevron.rides.driver.auth.model.getmodel.GetModelResponse
import com.mevron.rides.driver.home.model.HomeScreenResponse
import com.mevron.rides.driver.home.model.documents.DocumentStatusResponse
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class MevronRepo @Inject constructor (private val api: MevronAPI) {

    suspend fun registerPhone(data: RegisterBody): Response<RegisterResponse> {
        return api.registerPhone(data)

    }

    suspend fun validateOTP(data: ValidateOTPRequest): Response<OTPResponse> {
        return api.verifyOTP(data)

    }


    suspend fun createAccount(data: CreateAccountRequest): Response<GeneralResponse> {
        return api.createAccount(data)
    }

    suspend fun addVehicle(data: VehicleAddRequest): Response<GeneralResponse> {
        return api.addVehicle(data)
    }

    suspend fun uploadLicence(data: MultipartBody.Part): Response<GeneralResponse>{
        return api.uploadLicence(data)
    }

    suspend fun uploadProfile(data: MultipartBody.Part): Response<GeneralResponse>{
        return api.uploadProfile(data)
    }


    suspend fun uploadSticker(data: MultipartBody.Part): Response<GeneralResponse>{
        return api.uploadSticker(data)
    }

    suspend fun uploadInsurance(data: MultipartBody.Part): Response<GeneralResponse>{
        return api.uploadInsurance(data)
    }

    suspend fun uploadSecurityNum(data: SecurityNumRequest): Response<GeneralResponse> {
        return api.uploadSecurityNumber(data)
    }

    suspend fun getCarsMake(): Response<GetCallsResponse>{
        return api.getCarMakes()
    }

    suspend fun getCarsModel(param: HashMap<String, String>): Response<GetModelResponse>{
        return api.getCarModels(param)
    }

    suspend fun getCarsYear(param: HashMap<String, String>): Response<GetCarYear>{
        return api.getCarYear(param)
    }

    suspend fun tripManagement(data: TripManagementModel): Response<GeneralResponse>{
        return api.tripManagement(data)
    }

    suspend fun toggleStatus(): Response<GeneralResponse>{
        return  api.toggleStatus()
    }


    suspend fun getHomeStatus(): Response<HomeScreenResponse>{
        return api.getHomeStatus()
    }

    suspend fun getDocumentStatus(): Response<DocumentStatusResponse>{
        return  api.getDocumentStatus()
    }


}