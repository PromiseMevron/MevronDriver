package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.usecase

import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SaveDetailsRequest
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository.ISettingProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val repository: ISettingProfileRepository) {
    suspend operator fun invoke(data: SaveDetailsRequest) = repository.updateTheProfile(data)
}