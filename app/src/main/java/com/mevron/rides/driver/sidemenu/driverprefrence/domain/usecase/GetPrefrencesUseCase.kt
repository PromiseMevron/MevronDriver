package com.mevron.rides.driver.sidemenu.driverprefrence.domain.usecase

import com.mevron.rides.driver.sidemenu.driverprefrence.domain.repository.IPrefrenceRepository
import javax.inject.Inject

class GetPrefrencesUseCase @Inject constructor(private val repository: IPrefrenceRepository) {
    suspend operator fun invoke(email: String, token: String) =
        repository.getdriverPref(email = email, token = token)
}