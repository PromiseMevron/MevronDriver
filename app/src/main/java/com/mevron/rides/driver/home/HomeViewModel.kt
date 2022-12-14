package com.mevron.rides.driver.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mevron.rides.driver.authentication.domain.usecase.GetSharedPreferenceUseCase
import com.mevron.rides.driver.authentication.domain.usecase.GetSurgeUseCase
import com.mevron.rides.driver.authentication.domain.usecase.SetPreferenceUseCase
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.data.model.home.DeviceID
import com.mevron.rides.driver.home.domain.model.HomeScreenDomainModel
import com.mevron.rides.driver.home.domain.model.MapTripState
import com.mevron.rides.driver.home.domain.usecase.FCMTokenUseCase
import com.mevron.rides.driver.home.domain.usecase.GetDocumentStatusUseCase
import com.mevron.rides.driver.home.domain.usecase.GetMapTripStateUseCase
import com.mevron.rides.driver.home.domain.usecase.ToggleOnlineStatusUseCase
import com.mevron.rides.driver.home.trip_management.domain.usecase.TripManagementActionUseCase
import com.mevron.rides.driver.home.ui.DocumentSubmissionStatus
import com.mevron.rides.driver.home.ui.event.HomeViewEvent
import com.mevron.rides.driver.home.ui.state.HomeViewState
import com.mevron.rides.driver.home.ui.state.transform
import com.mevron.rides.driver.remote.TripManagementModel
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.GetProfileData
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.usecase.GetProfileUseCase
import com.mevron.rides.driver.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val onlineStatusUseCase: ToggleOnlineStatusUseCase,
    private val getDocumentStatusUseCase: GetDocumentStatusUseCase,
    private val getMapStateUseCase: GetMapTripStateUseCase,
    private val tripManageUseCase: TripManagementActionUseCase,
    private val preferenceUseCase: GetSharedPreferenceUseCase,
    private val setPreferenceUseCase: SetPreferenceUseCase,
    private val getSurgeUseCase: GetSurgeUseCase,
    private val profileUseCase: GetProfileUseCase,
    private val fcmTokenUseCase: FCMTokenUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<HomeViewState> =
        MutableStateFlow(HomeViewState.EMPTY)

    private val mutableSurgeState = MutableStateFlow("")

    val surgeState: StateFlow<String>
        get() = mutableSurgeState

    fun startObservingSurge() {
        viewModelScope.launch {
            getSurgeUseCase().collect {
                mutableSurgeState.value = it
            }
        }
    }

    fun updateErrorToEmpty(){
        mutableState.update {
            it.transform(
                errorMessage = "",
            )
        }
    }

    val state: StateFlow<HomeViewState>
        get() = mutableState


    fun updateCancelValue(value: String) {
        mutableState.update {
            it.transform(
                reasonForCancel = value,
            )
        }
    }

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

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = profileUseCase()) {

                is DomainModel.Success -> {
                    val gson = Gson()
                    val data = result.data as GetProfileData
                    val user = gson.toJson(data)
                    setPreferenceUseCase(Constants.SUPPORT_NUMBER, data.supportNumber ?: "")
                    setPreferenceUseCase(Constants.PROFILE,user )
                    mutableState.update {
                        it.transform(
                            gottenProfile = true,
                        )
                    }
                }
                else -> {}
            }
        }
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
                    trip_id = currentTripState.tripId ?: preferenceUseCase("TRIPID")

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
                    trip_id = currentTripState.tripId ?: preferenceUseCase("TRIPID")

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
                    trip_id = currentTripState.tripId ?: preferenceUseCase("TRIPID"),
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
                    trip_id = currentTripState.tripId ?: preferenceUseCase("TRIPID"),
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
                    trip_id = currentTripState.tripId ?: preferenceUseCase("TRIPID"),
                    rating = (rating.toDoubleOrNull() ?: 1.0).toInt()
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

    fun cancelRide() {
        val currentTripState = state.value.currentMapTripState
        Log.d("TAG", "crossed the check $currentTripState")
        //    if (currentTripState !is MapTripState.StartRideState) return
        Log.d("TAG", "crossed the check")
        viewModelScope.launch {
            val result = tripManageUseCase(
                TripManagementModel(
                    type = "cancel_ongoing",
                    trip_id = currentTripState.tripId ?: preferenceUseCase("TRIPID"),
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
                    trip_id = currentTripState.tripId ?: preferenceUseCase("TRIPID"),
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
            0 -> DocumentSubmissionStatus.NONE
            1 -> DocumentSubmissionStatus.REVIEW
            2 -> DocumentSubmissionStatus.REJECTED
            4 -> DocumentSubmissionStatus.EXPIRED
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

    fun updateToken(id: String) {
        Log.d("TOKEN FOR FIREBASE", "TOKEN FOR FIREBASE 2 $id")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("TOKEN FOR FIREBASE", "TOKEN FOR FIREBASE 3 $id")
            val result = fcmTokenUseCase(DeviceID(device_id = id))
            if (result is DomainModel.Success) {
                Log.d("TOKEN FOR FIREBASE", "TOKEN FOR FIREBASE 4 $id")
                mutableState.update {
                    it.transform(
                        tokenSuccessful = true
                    )
                }
            }

            if (result is DomainModel.Error) {
                Log.d("TOKEN FOR FIREBASE", "TOKEN FOR FIREBASE 5 $id")
                mutableState.update {
                    it.transform(
                        tokenSuccessful = true
                    )
                }
            }
        }
    }

}