package com.mevron.rides.driver.home.ui.state

import com.mevron.rides.driver.home.domain.model.MapTripState
import com.mevron.rides.driver.home.ui.DocumentSubmissionStatus

data class HomeViewState(
    val isDriveActive: Boolean,
    val isOnline: Boolean,
    val documentSubmissionStatus: DocumentSubmissionStatus,
    val errorMessage: String,
    val isLoadingDocuments: Boolean,
    val isLoadingOnlineStatus: Boolean,
    val isLocationUpdating: Boolean,
    val currentMapTripState: MapTripState
) {
    companion object {
        val EMPTY = HomeViewState(
            isDriveActive = true,
            isOnline = false,
            documentSubmissionStatus = DocumentSubmissionStatus.OKAY,
            errorMessage = "",
            isLoadingDocuments = false,
            isLoadingOnlineStatus = false,
            isLocationUpdating = false,
            currentMapTripState = MapTripState.Idle
        )
    }
}

fun HomeViewState.transform(
    isOnline: Boolean? = null,
    documentSubmissionStatus: DocumentSubmissionStatus? = null,
    isDriveActive: Boolean? = null,
    errorMessage: String? = null,
    isLoadingDocuments: Boolean? = null,
    isLoadingOnlineStatus: Boolean? = null,
    isLocationUpdating: Boolean? = null,
    mapTripState: MapTripState? = null
) = copy(
    isDriveActive = isDriveActive ?: this.isDriveActive,
    isOnline = isOnline ?: this.isOnline,
    documentSubmissionStatus = documentSubmissionStatus ?: this.documentSubmissionStatus,
    errorMessage = errorMessage ?: this.errorMessage,
    isLoadingDocuments = isLoadingDocuments ?: this.isLoadingDocuments,
    isLoadingOnlineStatus = isLoadingOnlineStatus ?: this.isLoadingOnlineStatus,
    isLocationUpdating = isLocationUpdating ?: this.isLocationUpdating,
    currentMapTripState = mapTripState ?: this.currentMapTripState
)