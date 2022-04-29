package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.model.AddVehicleRequest
import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import javax.inject.Inject

class AddVehicleUseCase @Inject constructor (private val repository: IUpdateProfileRepository) {

    suspend operator fun invoke(request: AddVehicleRequest) = repository.addVehicle(request)
}