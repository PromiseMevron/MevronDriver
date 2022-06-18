package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.usecase

import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository.ISettingProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val repository: ISettingProfileRepository) {
    suspend operator fun invoke() = repository.getProfile()
}