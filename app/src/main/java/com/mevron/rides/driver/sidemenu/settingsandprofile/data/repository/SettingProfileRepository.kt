package com.mevron.rides.driver.sidemenu.settingsandprofile.data.repository

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.GetProfileResponse
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.ReferalReport
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SaveDetailsRequest
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SetReferal
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.network.SettingProfileAPI
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.*
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository.ISettingProfileRepository
import com.mevron.rides.driver.util.Constants

class SettingProfileRepository(private val api: SettingProfileAPI) : ISettingProfileRepository {
    override suspend fun getProfile(): DomainModel = api.getProfile().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun updateTheProfile(data: SaveDetailsRequest): DomainModel =
        api.updateTheProfile(data).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel()
                    ?: DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(it)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        }

    override suspend fun resendEmailLink(): DomainModel = api.resendEmailLink().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel()
                ?: DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun signOut(): DomainModel  = api.signOut().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel()
                ?: DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }
    override suspend fun getAllReferal(): DomainModel {
        return try {
            val response = api.getReferralHistory()
            if (response.isSuccessful) {
                val data = response.body()?.success?.data?.result?.map {
                    ReferralData(it.category, it.createAt, it.description, it.id, it.title)
                } ?: mutableListOf()
                val dt = response.body()?.success?.data
                DomainModel.Success(
                    data = ReferralHistory(
                        referralData = data,
                        referralCode = dt?.referralCode,
                        referralStatus = dt?.referralStatus ?: 0
                    )
                )
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(response)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        } catch (error: Throwable) {
            DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun getAReferalFDetail(data: ReferalReport): DomainModel {
        return try {
            val response = api.getReferralReport(data)
            if (response.isSuccessful) {
                val numbers = response.body()?.success?.refData?.rides ?: "0"
                DomainModel.Success(data = ReferalNumber(rides = numbers))
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(response)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        } catch (error: Throwable) {
            DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun setReferral(data: SetReferal): DomainModel {
        return try {
            val response = api.setReferral(data)
            if (response.isSuccessful) {
                DomainModel.Success(data = Unit)
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(response)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        } catch (error: Throwable) {
            DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        }
    }

    private fun GetProfileResponse.toDomainModel() = DomainModel.Success(
        data = this.getProfileSuccess.profileData.apply {
            ProfileData(
                email ?: "",
                emailStatus,
                firstName ?: "",
                lastName ?: "",
                phoneNumber,
                phoneNumberStatus,
                profilePicture ?: "",
                rating ?: "",
                uuid = uuid ?: "",
                about = about,
                acceptanceRate = acceptanceRate,
                cancellationRate = cancellationRate,
                country = country,
                currency = currency,
                reviews = reviews,
                tripsCompleted = tripsCompleted,
                type = type ?: ""
            )
        }
    )

    private fun GeneralResponse.toDomainModel() = DomainModel.Success(
        data = this.success.apply {
            SettingsProfileResponse(message, status)
        }
    )
}