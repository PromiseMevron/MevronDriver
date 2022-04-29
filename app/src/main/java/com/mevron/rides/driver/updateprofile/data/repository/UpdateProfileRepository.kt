package com.mevron.rides.driver.updateprofile.data.repository

import com.mevron.rides.driver.data.model.DefaultResponse
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.updateprofile.data.network.UpdateProfileApi
import com.mevron.rides.driver.updateprofile.domain.model.AddVehicleRequest
import com.mevron.rides.driver.updateprofile.domain.model.SecurityNumRequest
import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import okhttp3.MultipartBody
import retrofit2.Response

class UpdateProfileRepository(
    private val updateProfileApi: UpdateProfileApi
) : IUpdateProfileRepository {

    override suspend fun addVehicle(data: AddVehicleRequest): DomainModel =
        mapToDomainModel(updateProfileApi.addVehicle(data))

    override suspend fun uploadLicence(image: MultipartBody.Part): DomainModel =
        mapToDomainModel(updateProfileApi.uploadLicence(image))

    override suspend fun uploadSticker(image: MultipartBody.Part): DomainModel =
        mapToDomainModel(updateProfileApi.uploadSticker(image))

    override suspend fun uploadProfile(image: MultipartBody.Part): DomainModel =
        mapToDomainModel(updateProfileApi.uploadProfile(image))

    override suspend fun uploadInsurance(image: MultipartBody.Part): DomainModel =
        mapToDomainModel(updateProfileApi.uploadInsurance(image))

    override suspend fun uploadSecurityNumber(data: SecurityNumRequest): DomainModel =
        mapToDomainModel(updateProfileApi.uploadSecurityNumber(data))

    private fun mapToDomainModel(it: Response<DefaultResponse>) =
        if (it.isSuccessful) {
            DomainModel.Success(data = Unit)
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
}