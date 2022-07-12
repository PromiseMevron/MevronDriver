package com.mevron.rides.driver.sidemenu.vehicle.domain.repository

import com.mevron.rides.driver.domain.DomainModel

interface IVehicleRepository {
    suspend fun getVehicles(): DomainModel
    suspend fun getVehicleDetails(id: String): DomainModel
    suspend fun deleteVehicle(id: String): DomainModel
}