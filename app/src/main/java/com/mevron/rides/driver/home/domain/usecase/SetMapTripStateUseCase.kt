package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.IMapStateRepository
import com.mevron.rides.driver.home.domain.model.MapTripState
import javax.inject.Inject

class SetMapTripStateUseCase @Inject constructor(
    private val repository: IMapStateRepository
) {
    operator fun invoke(mapTripState: MapTripState) = repository.setCurrentState(mapTripState)
}