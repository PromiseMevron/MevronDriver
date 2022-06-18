package com.mevron.rides.driver.trips.domain.usecase

import com.mevron.rides.driver.trips.domain.repository.IGetTripsRepository
import javax.inject.Inject

class GetTripUseCase @Inject constructor(private val repository: IGetTripsRepository) {
    suspend operator fun invoke(start: String, end: String) =
        repository.getAllTrips(start = start, end = end)
}