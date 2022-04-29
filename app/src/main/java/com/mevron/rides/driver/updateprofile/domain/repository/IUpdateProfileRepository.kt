package com.mevron.rides.driver.updateprofile.domain.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.updateprofile.domain.model.AddVehicleRequest
import com.mevron.rides.driver.updateprofile.domain.model.SecurityNumRequest
import okhttp3.MultipartBody

interface IUpdateProfileRepository {

    suspend fun addVehicle(data: AddVehicleRequest): DomainModel

    suspend fun uploadLicence(image: MultipartBody.Part): DomainModel

    suspend fun uploadSticker(image: MultipartBody.Part): DomainModel

    suspend fun uploadProfile(image: MultipartBody.Part): DomainModel

    suspend fun uploadInsurance(image: MultipartBody.Part): DomainModel

    suspend fun uploadSecurityNumber(data: SecurityNumRequest): DomainModel
}