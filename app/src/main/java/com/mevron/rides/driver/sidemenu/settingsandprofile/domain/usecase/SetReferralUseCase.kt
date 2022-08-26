package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.usecase

import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SetReferal
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository.ISettingProfileRepository
import javax.inject.Inject

class SetReferralUseCase @Inject constructor(private val repository: ISettingProfileRepository) {
    suspend operator fun invoke(data: SetReferal) = repository.setReferral(data)
}