package com.mevron.rides.driver.home.domain

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.data.model.home.DeviceID

interface IHomeScreenRepository {
    suspend fun toggleStatus(): DomainModel
    suspend fun getDocumentStatus(): DomainModel
    suspend fun sendToken(id : DeviceID): DomainModel
}