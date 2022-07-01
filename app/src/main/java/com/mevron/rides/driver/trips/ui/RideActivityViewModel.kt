package com.mevron.rides.driver.trips.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.home.data.model.home.Trip
import com.mevron.rides.driver.trips.domain.model.GetTripDomainData
import com.mevron.rides.driver.trips.domain.usecase.GetTripUseCase
import com.mevron.rides.driver.trips.ui.event.GetTripsEvent
import com.mevron.rides.driver.trips.ui.state.GetTripState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideActivityViewModel @Inject constructor(private val useCase: GetTripUseCase) : ViewModel() {

    private val mutableState: MutableStateFlow<GetTripState> =
        MutableStateFlow(GetTripState.EMPTY)
    val state: StateFlow<GetTripState>
        get() = mutableState

    fun handleEvent(event: GetTripsEvent) {
        when (event) {
            GetTripsEvent.GetTrips -> getRides()
        }
    }

    private fun getRides() {
        updateState(isLoading = true)
        val startDate = mutableState.value.startDate
        val endDate = mutableState.value.endDate
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase(start = startDate, endDate)) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        error = "failure to fetch previous trips"
                    )
                }
                is DomainModel.Success -> {
                    val data = result.data as GetTripDomainData
                    updateState(
                        isLoading = false,
                        data = data,
                        items = data.buildActivityData()
                    )
                }
            }
        }
    }

    private fun GetTripDomainData.buildActivityData(): MutableList<RideActivityDataClass> {
        val arr = this.results
        arr.sortedBy { it.date }
        val theData: MutableList<RideActivityDataClass> = mutableListOf()
        var arr2: MutableList<Trip> = mutableListOf()
        var date = ""
        arr.forEachIndexed { index, element ->
            if (index == 0) {
                date = element.date
                arr2.add(Trip(amount = element.amount, name = element.title, time = element.time))
            } else {
                if (arr[index - 1] == arr[index]) {
                    arr2.add(
                        Trip(
                            amount = element.amount,
                            name = element.title,
                            time = element.time
                        )
                    )
                } else {
                    theData.add(RideActivityDataClass(date = date, items = arr2))
                    arr2 = mutableListOf()
                    date = element.date
                    arr2.add(
                        Trip(
                            amount = element.amount,
                            name = element.title,
                            time = element.time
                        )
                    )
                }
            }
            if (index == arr.size - 1) {
                theData.add(RideActivityDataClass(date = date, items = arr2))
            }
        }
        return theData
    }


    fun updateState(
        startDate: String? = null,
        endDate: String? = null,
        data: GetTripDomainData? = null,
        items: MutableList<RideActivityDataClass>? = null,
        isLoading: Boolean? = null,
        isRequestSuccess: Boolean? = null,
        error: String? = null,
        displayDate: String? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                isLoading = isLoading ?: currentState.isLoading,
                error = error ?: currentState.error,
                startDate = startDate ?: currentState.startDate,
                endDate = endDate ?: currentState.endDate,
                data = data ?: currentState.data,
                items = items ?: currentState.items,
                isRequestSuccess = isRequestSuccess ?: currentState.isRequestSuccess,
                displayDate = displayDate ?: currentState.displayDate
            )
        }
    }
}