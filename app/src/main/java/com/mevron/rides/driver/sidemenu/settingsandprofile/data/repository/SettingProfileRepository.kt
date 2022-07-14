package com.mevron.rides.driver.sidemenu.settingsandprofile.data.repository

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.GetProfileResponse
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SaveDetailsRequest
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.network.SettingProfileAPI
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.ProfileData
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.SettingsProfileResponse
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository.ISettingProfileRepository

class SettingProfileRepository(private val api: SettingProfileAPI) : ISettingProfileRepository {
    override suspend fun getProfile(): DomainModel = api.getProfile().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Error in getting profile"))
        } else {
            DomainModel.Error(Throwable("Error in getting profile"))
        }
    }

    override suspend fun updateTheProfile(data: SaveDetailsRequest): DomainModel =
        api.updateTheProfile(data).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel()
                    ?: DomainModel.Error(Throwable("Error in updating the profile"))
            } else {
                DomainModel.Error(Throwable("Error in updating the profile"))
            }
        }

    override suspend fun resendEmailLink(): DomainModel = api.resendEmailLink().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel()
                ?: DomainModel.Error(Throwable("Error in resending the email verification mail"))
        } else {
            DomainModel.Error(Throwable("Error in resending the email verification mail"))
        }
    }

    override suspend fun signOut(): DomainModel  = api.signOut().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel()
                ?: DomainModel.Error(Throwable("Error in signing out"))
        } else {
            DomainModel.Error(Throwable("Error in signing out"))
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
                uuid ?: "",
                about = about,
                acceptanceRate = acceptanceRate,
                cancellationRate = cancellationRate,
                country = country,
                currency = currency,
                reviews = reviews,
                tripsCompleted = tripsCompleted,
            )
        }
    )

    private fun GeneralResponse.toDomainModel() = DomainModel.Success(
        data = this.success.apply {
            SettingsProfileResponse(message, status)
        }
    )
}