package com.mevron.rides.driver.sidemenu.driverprefrence.domain.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.PrefrenceData

interface IPrefrenceRepository {
    suspend fun getdriverPref(email: String, token: String): DomainModel
    suspend fun savedriverPref(data: PrefrenceData): DomainModel
}