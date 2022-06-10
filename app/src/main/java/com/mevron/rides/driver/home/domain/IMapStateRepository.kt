package com.mevron.rides.driver.home.domain

import com.mevron.rides.driver.home.domain.model.MapTripState
import kotlinx.coroutines.flow.StateFlow

interface IMapStateRepository {

    val currentMapTripState: StateFlow<MapTripState>

    fun setCurrentState(mapTripState: MapTripState)
}