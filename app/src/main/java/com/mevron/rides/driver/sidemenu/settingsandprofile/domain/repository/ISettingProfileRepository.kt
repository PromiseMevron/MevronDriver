package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SaveDetailsRequest

interface ISettingProfileRepository {
    suspend fun getProfile(): DomainModel
    suspend fun updateTheProfile(data: SaveDetailsRequest): DomainModel
    suspend fun resendEmailLink(): DomainModel
}