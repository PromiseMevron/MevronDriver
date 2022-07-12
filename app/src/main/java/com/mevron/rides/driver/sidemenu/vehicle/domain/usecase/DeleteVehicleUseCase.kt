package com.mevron.rides.driver.sidemenu.vehicle.domain.usecase

import com.mevron.rides.driver.sidemenu.vehicle.domain.repository.IVehicleRepository
import javax.inject.Inject

class DeleteVehicleUseCase @Inject constructor(private val repository: IVehicleRepository) {
    suspend operator fun invoke(id: String) = repository.deleteVehicle(id)
}