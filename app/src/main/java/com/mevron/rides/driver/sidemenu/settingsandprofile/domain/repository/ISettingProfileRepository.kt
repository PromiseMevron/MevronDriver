package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.ReferalReport
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SaveDetailsRequest
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SetReferal

interface ISettingProfileRepository {
    suspend fun getProfile(): DomainModel
    suspend fun updateTheProfile(data: SaveDetailsRequest): DomainModel
    suspend fun resendEmailLink(): DomainModel
    suspend fun signOut(): DomainModel
    suspend fun getAllReferal(): DomainModel
    suspend fun getAReferalFDetail(data: ReferalReport): DomainModel
    suspend fun setReferral(data: SetReferal): DomainModel
}