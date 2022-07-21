package com.mevron.rides.driver.location.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.location.domain.model.LocationData
import com.mevron.rides.driver.location.domain.usecase.UserLiveLocationUseCase
import com.mevron.rides.driver.location.domain.usecase.UserCurrentLocationUseCase
import com.mevron.rides.driver.location.domain.usecase.StartLocationUpdatesUseCase
import com.mevron.rides.driver.location.domain.usecase.StopLocationUpdatesUseCase
import com.mevron.rides.driver.location.ui.event.LocationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val startLocationUpdatesUseCase: StartLocationUpdatesUseCase,
    private val stopLocationUpdatesUseCase: StopLocationUpdatesUseCase,
    private val lastLocationUseCas: UserLiveLocationUseCase,
    private val liveLocationUseCase: UserCurrentLocationUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<LocationData?> = MutableStateFlow(null)

    val state: StateFlow<LocationData?>
        get() = mutableState

    private var isLocationLoaded: Boolean = false

    val liveLocation: StateFlow<LocationData> = liveLocationUseCase()

    val currentLocationState: StateFlow<LocationData?> = lastLocationUseCas()

    private fun requestLastLocation() {
        val currentState = mutableState.value
        viewModelScope.launch {
           lastLocationUseCas().collect {
               it?.let { locationData ->
                   mutableState.update {
                       currentState?.copy(
                           lat = locationData.lat,
                           long = locationData.long,
                           direction = locationData.direction,
                           uuid = locationData.uuid,
                           isForeground = locationData.isForeground
                       )
                   }
               }
           }
        }
    }

    fun onEventReceived(event: LocationEvent) {
        when (event) {
            LocationEvent.StartLocationUpdate -> startLocationUpdatesUseCase()
            LocationEvent.StopLocationUpdate -> stopLocationUpdatesUseCase()
            LocationEvent.RequestLastLocation -> requestLastLocation()
        }
    }

    fun locationNotLoaded(): Boolean = isLocationLoaded
    fun setLocationLoaded(isLocationLoaded: Boolean) {
        this.isLocationLoaded = isLocationLoaded
    }
}