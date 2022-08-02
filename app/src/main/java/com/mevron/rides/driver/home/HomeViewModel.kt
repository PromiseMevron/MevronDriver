package com.mevron.rides.driver.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import com.mevron.rides.driver.authentication.domain.usecase.GetSharedPreferenceUseCase
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.domain.model.HomeScreenDomainModel
import com.mevron.rides.driver.home.domain.model.MapTripState
import com.mevron.rides.driver.home.domain.usecase.GetDocumentStatusUseCase
import com.mevron.rides.driver.home.domain.usecase.GetMapTripStateUseCase
import com.mevron.rides.driver.home.domain.usecase.ToggleOnlineStatusUseCase
import com.mevron.rides.driver.home.trip_management.domain.usecase.TripManagementActionUseCase
import com.mevron.rides.driver.home.ui.DocumentSubmissionStatus
import com.mevron.rides.driver.home.ui.event.HomeViewEvent
import com.mevron.rides.driver.home.ui.state.HomeViewState
import com.mevron.rides.driver.home.ui.state.transform
import com.mevron.rides.driver.remote.TripManagementModel
import com.mevron.rides.driver.remote.geoservice.domain.usecase.GetGoogleResponseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val onlineStatusUseCase: ToggleOnlineStatusUseCase,
    private val getDocumentStatusUseCase: GetDocumentStatusUseCase,
    private val getMapStateUseCase: GetMapTripStateUseCase,
    private val tripManageUseCase: TripManagementActionUseCase,
    private val preferenceUseCase: GetSharedPreferenceUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<HomeViewState> =
        MutableStateFlow(HomeViewState.EMPTY)

    val state: StateFlow<HomeViewState>
        get() = mutableState

    fun onEventReceived(event: HomeViewEvent) =
        when (event) {
            HomeViewEvent.OnDocumentSubmissionStatusClick -> getDocument()
            HomeViewEvent.OnDriveClick -> mutableState.update {
                it.transform(
                    isDriveActive = true,
                    errorMessage = ""
                )
            }
            HomeViewEvent.OnEarningClick -> mutableState.update {
                it.transform(
                    isDriveActive = false,
                    errorMessage = ""
                )
            }
            HomeViewEvent.OnToggleOnlineClick -> toggleOnlineStatus()
            is HomeViewEvent.LocationStarted -> mutableState.update { it.transform(errorMessage = "") }
            HomeViewEvent.AcceptRideClick -> acceptRide()
            HomeViewEvent.StartRideClick -> startRide(code = "")
        }

    private fun acceptRide() {
        val currentTripState = state.value.currentMapTripState
        Log.d("TAG", "crossed the check $currentTripState")
        if (currentTripState !is MapTripState.AcceptRideState) return
        Log.d("TAG", "crossed the check")
        viewModelScope.launch {
            val result = tripManageUseCase(
                TripManagementModel(
                    type = "accept",
                    trip_id = currentTripState.tripId ?: ""

                )
            )
            if (result is DomainModel.Success) {
                mutableState.update { it.transform(getStatus = true) }
                // update This should be a fire and forget

            } else {
                mutableState.update { it.transform(errorMessage = (result as DomainModel.Error).error.localizedMessage) }
            }
        }
    }

     fun arrivedRide() {
        val currentTripState = state.value.currentMapTripState
        Log.d("TAG", "crossed the check $currentTripState")
       // if (currentTripState !is MapTripState.ApproachingPassengerState) return
        Log.d("TAG", "crossed the check")
        viewModelScope.launch {
            val result = tripManageUseCase(
                TripManagementModel(
                    type = "driver_arrived",
                    trip_id = currentTripState.tripId ?: ""

                )
            )
            if (result is DomainModel.Success) {
                mutableState.update { it.transform(getStatus = true) }
                // update This should be a fire and forget

            } else {
                mutableState.update { it.transform(errorMessage = (result as DomainModel.Error).error.localizedMessage) }
            }
        }
    }

    fun completeRide() {
        val currentTripState = state.value.currentMapTripState
        Log.d("TAG", "crossed the check $currentTripState")
        //    if (currentTripState !is MapTripState.StartRideState) return
        Log.d("TAG", "crossed the check")
        viewModelScope.launch {
            val result = tripManageUseCase(
                TripManagementModel(
                    type = "completed",
                    trip_id = currentTripState.tripId ?: "",
                )
            )
            if (result is DomainModel.Success) {
                mutableState.update { it.transform(getStatus = true) }
                // update This should be a fire and forget

            } else {
                mutableState.update { it.transform(errorMessage = (result as DomainModel.Error).error.localizedMessage) }
            }
        }
    }

     fun startRide(code: String) {
        val currentTripState = state.value.currentMapTripState
        Log.d("TAG", "crossed the check $currentTripState")
    //    if (currentTripState !is MapTripState.StartRideState) return
        Log.d("TAG", "crossed the check")
        viewModelScope.launch {
            val result = tripManageUseCase(
                TripManagementModel(
                    type = "trip_began",
                    trip_id = currentTripState.tripId ?: "",
                    code = code
                )
            )
            if (result is DomainModel.Success) {
                mutableState.update { it.transform(getStatus = true) }
                // update This should be a fire and forget

            } else {
                mutableState.update { it.transform(errorMessage = (result as DomainModel.Error).error.localizedMessage) }
            }
        }
    }

    fun rateRide(rating: String) {
        val currentTripState = state.value.currentMapTripState
        Log.d("TAG", "crossed the check $currentTripState")
        //    if (currentTripState !is MapTripState.StartRideState) return
        Log.d("TAG", "crossed the check")
        viewModelScope.launch {
            val result = tripManageUseCase(
                TripManagementModel(
                    type = "rate",
                    trip_id = currentTripState.tripId ?: "",
                    rating = rating.toInt()
                )
            )
            if (result is DomainModel.Success) {
                mutableState.update { it.transform(getStatus = true) }
                // update This should be a fire and forget

            } else {
                mutableState.update { it.transform(errorMessage = (result as DomainModel.Error).error.localizedMessage) }
            }
        }
    }

    fun collectCash(amount: String) {
        val currentTripState = state.value.currentMapTripState
        Log.d("TAG", "crossed the check $currentTripState")
        //    if (currentTripState !is MapTripState.StartRideState) return
        Log.d("TAG", "crossed the check")
        viewModelScope.launch {
            val result = tripManageUseCase(
                TripManagementModel(
                    type = "cash_collected",
                    trip_id = currentTripState.tripId ?: "",
                    amount = amount
                )
            )
            if (result is DomainModel.Success) {
                mutableState.update { it.transform(getStatus = true) }
                // update This should be a fire and forget

            } else {
                mutableState.update { it.transform(errorMessage = (result as DomainModel.Error).error.localizedMessage) }
            }
        }
    }

    private fun toggleOnlineStatus() {
        mutableState.update { it.transform(isOnline = !it.isOnline) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = onlineStatusUseCase()
            if (result is DomainModel.Success) {
                getDocument()
            }

            if (result is DomainModel.Error) {
                mutableState.update {
                    it.transform(
                        isOnline = !it.isOnline,
                        errorMessage = "Error occurred while changing online status"
                    )
                }
            }
        }
    }

    private fun getDocument() {
        mutableState.update { it.transform(isLoadingDocuments = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = getDocumentStatusUseCase()
            if (result is DomainModel.Success) {
                val data = result.data as HomeScreenDomainModel
                // update state
                mutableState.update {
                    it.transform(
                        documentSubmissionStatus = convertToDocumentStatus(
                            data.documentStatus
                        )
                    )
                }
                mutableState.update { it.transform(isLoadingDocuments = false, errorMessage = "") }
                mutableState.update { it.transform(weeklyChallenge = data.drive.weeklyChallenges, errorMessage = "") }
                mutableState.update { it.transform(scheduledPickup = data.drive.scheduledPickups, errorMessage = "") }
                mutableState.update { it.transform(isOnline = data.onlineStatus, errorMessage = "") }
                mutableState.update { it.transform(earnings = data.earnings, errorMessage = "") }
            }
        }
    }

    init {
        viewModelScope.launch {
            getMapStateUseCase().collect { mapState ->
                mutableState.update { it.transform(mapTripState = mapState, errorMessage = "") }
            }
        }
    }

    private fun convertToDocumentStatus(value: Int): DocumentSubmissionStatus {
        return when (value) {
            0 -> DocumentSubmissionStatus.PENDING
            1 -> DocumentSubmissionStatus.REVIEW
            else -> DocumentSubmissionStatus.OKAY
        }
    }

    private fun triStateAction(data: TripManagementModel) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun updateStatusLoading(){
        mutableState.update { it.transform(getStatus = false) }
    }

}