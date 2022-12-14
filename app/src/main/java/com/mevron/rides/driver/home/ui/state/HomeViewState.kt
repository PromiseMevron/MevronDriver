package com.mevron.rides.driver.home.ui.state

import com.mevron.rides.driver.home.data.model.home.*
import com.mevron.rides.driver.home.domain.model.MapTripState
import com.mevron.rides.driver.home.ui.DocumentSubmissionStatus

data class SurgeUrlState(
    val surgeUrl: String
)

data class HomeViewState(
    val isDriveActive: Boolean,
    val isOnline: Boolean,
    val documentSubmissionStatus: DocumentSubmissionStatus,
    val errorMessage: String,
    val isLoadingDocuments: Boolean,
    val isLoadingOnlineStatus: Boolean,
    val isLocationUpdating: Boolean,
    val currentMapTripState: MapTripState,
    val weeklyChallenge: List<WeeklyChallenge>,
    val scheduledPickup: List<ScheduledPickup>,
    val earnings: Earnings,
    val getStatus: Boolean,
    val tripId: String,
    val collectedAmount: String,
    val tokenSuccessful: Boolean,
    val deviceID: String,
    val reasonForCancel: String,
    val gottenProfile: Boolean
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
            currentMapTripState = MapTripState.Idle,
            weeklyChallenge = mutableListOf(),
            scheduledPickup = mutableListOf(),
            getStatus = false,
            deviceID = "",
            reasonForCancel = "",
            tokenSuccessful = false,
            gottenProfile = false,
            earnings = Earnings(
                balance = "",
                currency = "",
                earningGoal = EarningGoal(
                    earned_goal = "",
                    expiryDate = "",
                    percentage = "0",
                    weeklyGoal = ""
                ),
                nextPaymentDate = "",
                todayActivity = TodayActivityX(
                    earnings = "",
                    online = "",
                    rides = 0,
                    trip_list = mutableListOf()
                ),
                weeklySummary = WeeklySummary(
                    earnings = "",
                    online = "",
                    rides = 0,
                    tripList = mutableListOf()
                ),
                currencySymbol = ""
            ),
            tripId = "",
            collectedAmount = ""
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
    mapTripState: MapTripState? = null,
    getStatus: Boolean? = null,
    weeklyChallenge: List<WeeklyChallenge>? = null,
    scheduledPickup: List<ScheduledPickup>? = null,
    earnings: Earnings? = null,
    tripId: String? = null,
    collectedAmount: String? = null,
    tokenSuccessful: Boolean? = null,
    deviceID: String? = null,
    reasonForCancel: String? = null,
    gottenProfile: Boolean? = null
) = copy(
    isDriveActive = isDriveActive ?: this.isDriveActive,
    isOnline = isOnline ?: this.isOnline,
    documentSubmissionStatus = documentSubmissionStatus ?: this.documentSubmissionStatus,
    errorMessage = errorMessage ?: this.errorMessage,
    isLoadingDocuments = isLoadingDocuments ?: this.isLoadingDocuments,
    isLoadingOnlineStatus = isLoadingOnlineStatus ?: this.isLoadingOnlineStatus,
    isLocationUpdating = isLocationUpdating ?: this.isLocationUpdating,
    currentMapTripState = mapTripState ?: this.currentMapTripState,
    weeklyChallenge = weeklyChallenge ?: this.weeklyChallenge,
    scheduledPickup = scheduledPickup ?: this.scheduledPickup,
    earnings = earnings ?: this.earnings,
    getStatus = getStatus ?: this.getStatus,
    tripId = tripId ?: this.tripId,
    collectedAmount = collectedAmount ?: this.collectedAmount,
    deviceID = deviceID ?: this.deviceID,
    tokenSuccessful = tokenSuccessful ?: this.tokenSuccessful,
    reasonForCancel = reasonForCancel ?: this.reasonForCancel,
    gottenProfile = gottenProfile ?: this.gottenProfile
)