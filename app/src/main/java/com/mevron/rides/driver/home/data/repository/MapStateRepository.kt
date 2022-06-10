package com.mevron.rides.driver.home.data.repository

import com.mevron.rides.driver.home.domain.IMapStateRepository
import com.mevron.rides.driver.home.domain.model.MapTripState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The MapState will be tracked by this repository, this would be set by the socket
 * or http request to when trip status is obtained.
 * Furthermore, as trip progress changes, the app will update this repository to track
 * trip state.
 */
/**
 * TODO This class is not complete, write unit test upon completion of this class
 */
class MapStateRepository : IMapStateRepository {

    override val currentMapTripState: StateFlow<MapTripState>
        get() = mutableState

    private var mutableState: MutableStateFlow<MapTripState> = MutableStateFlow(MapTripState.Idle)

    override fun setCurrentState(mapTripState: MapTripState) {
        mutableState.value = mapTripState
    }
}