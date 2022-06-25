package com.mevron.rides.driver.home.trip_management.domain.usecase

import com.mevron.rides.driver.home.trip_management.domain.repository.ITripManagementRepository
import com.mevron.rides.driver.remote.TripManagementModel
import javax.inject.Inject

class TripManagementActionUseCase @Inject constructor(private val repository: ITripManagementRepository) {
    suspend operator fun invoke(data: TripManagementModel) = repository.tripManagement(data = data)
}