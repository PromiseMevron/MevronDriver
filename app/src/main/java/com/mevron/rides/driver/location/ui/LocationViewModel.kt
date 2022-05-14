package com.mevron.rides.driver.location.ui

import androidx.lifecycle.ViewModel
import com.mevron.rides.driver.location.domain.usecase.StartLocationUpdatesUseCase
import com.mevron.rides.driver.location.domain.usecase.StopLocationUpdatesUseCase
import com.mevron.rides.driver.location.ui.event.LocationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val startLocationUpdatesUseCase: StartLocationUpdatesUseCase,
    private val stopLocationUpdatesUseCase: StopLocationUpdatesUseCase
) : ViewModel() {

    fun onEventReceived(event: LocationEvent) {
        when(event) {
            LocationEvent.StartLocationUpdate -> startLocationUpdatesUseCase()
            LocationEvent.StopLocationUpdate -> stopLocationUpdatesUseCase()
        }
    }
}