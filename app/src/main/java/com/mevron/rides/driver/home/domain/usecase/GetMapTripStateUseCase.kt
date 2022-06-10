package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.IMapStateRepository
import javax.inject.Inject

class GetMapTripStateUseCase @Inject constructor(
    val repository: IMapStateRepository
) {
    operator fun invoke() = repository.currentMapTripState
}