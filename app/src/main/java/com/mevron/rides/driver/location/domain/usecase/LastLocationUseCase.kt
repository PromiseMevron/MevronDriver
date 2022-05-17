package com.mevron.rides.driver.location.domain.usecase

import com.mevron.rides.driver.location.domain.repository.ILocationRepository
import javax.inject.Inject

// TODO Complete the unit test of this package com.mevron.rides.driver.location.domain.usecase
class LastLocationUseCase @Inject constructor(private val locationRepository: ILocationRepository) {
    operator fun invoke() = locationRepository.currentLocationAtNow
}