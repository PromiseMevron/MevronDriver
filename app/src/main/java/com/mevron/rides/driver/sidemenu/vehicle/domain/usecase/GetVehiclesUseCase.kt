package com.mevron.rides.driver.sidemenu.vehicle.domain.usecase

import com.mevron.rides.driver.sidemenu.vehicle.domain.repository.IVehicleRepository
import javax.inject.Inject

class GetVehiclesUseCase @Inject constructor(private val repository: IVehicleRepository) {
    suspend operator fun invoke() = repository.getVehicles()
}