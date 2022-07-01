package com.mevron.rides.driver.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.domain.model.HomeScreenDomainModel
import com.mevron.rides.driver.home.domain.usecase.GetDocumentStatusUseCase
import com.mevron.rides.driver.home.domain.usecase.GetMapTripStateUseCase
import com.mevron.rides.driver.home.domain.usecase.ToggleOnlineStatusUseCase
import com.mevron.rides.driver.home.ui.DocumentSubmissionStatus
import com.mevron.rides.driver.home.ui.event.HomeViewEvent
import com.mevron.rides.driver.home.ui.state.HomeViewState
import com.mevron.rides.driver.home.ui.state.transform
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : ViewModel() {

    private val mutableState: MutableStateFlow<HomeViewState> =
        MutableStateFlow(HomeViewState.EMPTY)

    val state: StateFlow<HomeViewState>
        get() = mutableState

    fun onEventReceived(event: HomeViewEvent) =
        when (event) {
            HomeViewEvent.OnDocumentSubmissionStatusClick -> getDocument()
            HomeViewEvent.OnDriveClick -> mutableState.update { it.transform(isDriveActive = true) }
            HomeViewEvent.OnEarningClick -> mutableState.update { it.transform(isDriveActive = false) }
            HomeViewEvent.OnToggleOnlineClick -> toggleOnlineStatus()
            is HomeViewEvent.LocationStarted -> mutableState.update { it.transform() }
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
                mutableState.update { it.transform(isLoadingDocuments = false) }
                mutableState.update { it.transform(weeklyChallenge = data.drive.weeklyChallenges) }
                mutableState.update { it.transform(scheduledPickup = data.drive.scheduledPickups) }
                mutableState.update { it.transform(isOnline = data.onlineStatus) }
                mutableState.update { it.transform(earnings = data.earnings) }
            }
        }
    }

    init {
        viewModelScope.launch {
            getMapStateUseCase().collect { mapState ->
                mutableState.update { it.transform(mapTripState = mapState) }
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
}