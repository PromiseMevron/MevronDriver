package com.mevron.rides.driver.home.domain

import com.mevron.rides.driver.home.domain.model.MapTripState
import kotlinx.coroutines.flow.StateFlow

interface ISocketDataRepository {
    fun setCurrentState(mapTripState: MapTripState)
    val getStateMachineState: StateFlow<MapTripState>
}