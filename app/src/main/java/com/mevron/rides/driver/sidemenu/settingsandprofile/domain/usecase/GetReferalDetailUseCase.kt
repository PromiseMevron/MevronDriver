package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.usecase

import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.ReferalReport
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository.ISettingProfileRepository
import javax.inject.Inject

class GetReferalDetailUseCase @Inject constructor(private val repository: ISettingProfileRepository) {
    suspend operator fun invoke(data: ReferalReport) = repository.getAReferalFDetail(data)
}