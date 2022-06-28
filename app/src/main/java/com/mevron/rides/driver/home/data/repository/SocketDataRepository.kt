package com.mevron.rides.driver.home.data.repository

import com.mevron.rides.driver.home.domain.ISocketDataRepository
import com.mevron.rides.driver.home.domain.model.MapTripState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SocketDataRepository(
) : ISocketDataRepository {
    override val getStateMachineState: StateFlow<MapTripState>
        get() = mutableState

    private var mutableState: MutableStateFlow<MapTripState> = MutableStateFlow(MapTripState.Idle)

    override fun setCurrentState(mapTripState: MapTripState) {
        mutableState.value = mapTripState
    }
}