package com.mevron.rides.driver.trips.domain.repository

import com.mevron.rides.driver.domain.DomainModel

interface IGetTripsRepository {
    suspend fun getAllTrips(start: String, end: String): DomainModel
}