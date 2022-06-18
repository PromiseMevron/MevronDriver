package com.mevron.rides.driver.home.trip_management.domain.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.remote.TripManagementModel

interface ITripManagementRepository {
    suspend fun tripManagement(data: TripManagementModel): DomainModel
}