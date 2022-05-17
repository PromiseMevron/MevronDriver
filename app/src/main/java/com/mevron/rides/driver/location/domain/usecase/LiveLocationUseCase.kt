package com.mevron.rides.driver.location.domain.usecase

import com.mevron.rides.driver.location.domain.repository.ILocationRepository
import javax.inject.Inject

class LiveLocationUseCase @Inject constructor (private val locationRepository: ILocationRepository) {
    operator fun invoke() = locationRepository.lastLocation
}