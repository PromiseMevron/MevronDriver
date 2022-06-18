package com.mevron.rides.driver.sidemenu.driverprefrence.domain.usecase

import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.PrefrenceData
import com.mevron.rides.driver.sidemenu.driverprefrence.domain.repository.IPrefrenceRepository
import javax.inject.Inject

class SavePrefrenceUseCase @Inject constructor(private val repository: IPrefrenceRepository) {
    suspend operator fun invoke(data: PrefrenceData) =
        repository.savedriverPref(data)
}