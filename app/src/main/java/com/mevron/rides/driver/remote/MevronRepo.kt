package com.mevron.rides.driver.remote

import com.mevron.rides.driver.auth.model.*
import com.mevron.rides.driver.auth.model.caryear.GetCarYear
import com.mevron.rides.driver.auth.model.getcar.GetCallsResponse
import com.mevron.rides.driver.auth.model.getmodel.GetModelResponse
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


    suspend fun createAccount(data: CreateAccountRequest): Response<CreateResponse> {
        return api.createAccount(data)
    }

    suspend fun addVehicle(data: VehicleAddRequest): Response<CreateResponse> {
        return api.addVehicle(data)
    }

    suspend fun uploadLicence(data: MultipartBody.Part): Response<CreateResponse>{
        return api.uploadLicence(data)
    }

    suspend fun uploadProfile(data: MultipartBody.Part): Response<CreateResponse>{
        return api.uploadProfile(data)
    }


    suspend fun uploadSticker(data: MultipartBody.Part): Response<CreateResponse>{
        return api.uploadSticker(data)
    }

    suspend fun uploadInsurance(data: MultipartBody.Part): Response<CreateResponse>{
        return api.uploadInsurance(data)
    }

    suspend fun uploadSecurityNum(data: SecurityNumRequest): Response<CreateResponse> {
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


}