package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.ISocketDataRepository
import com.mevron.rides.driver.home.domain.model.MapTripState
import javax.inject.Inject

class SetSocketStatusUseCase @Inject constructor(
    private val repository: ISocketDataRepository
) {
    operator fun invoke(mapTripState: MapTripState) = repository.setCurrentState(mapTripState)
}