package com.mevron.rides.driver.sidemenu.driverprefrence.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.authentication.domain.usecase.GetSharedPreferenceUseCase
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.PrefrenceData
import com.mevron.rides.driver.sidemenu.driverprefrence.domain.model.PrefrenceDomainData
import com.mevron.rides.driver.sidemenu.driverprefrence.domain.usecase.GetPrefrencesUseCase
import com.mevron.rides.driver.sidemenu.driverprefrence.domain.usecase.SavePrefrenceUseCase
import com.mevron.rides.driver.sidemenu.driverprefrence.ui.event.DriverPrefrenceEvent
import com.mevron.rides.driver.sidemenu.driverprefrence.ui.state.DriverPrefenceState
import com.mevron.rides.driver.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverPrefrenceViewModel @Inject constructor(
    private val useCase: GetPrefrencesUseCase,
    private val setUseCase: SavePrefrenceUseCase,
    private val sharedPref: GetSharedPreferenceUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<DriverPrefenceState> =
        MutableStateFlow(DriverPrefenceState.EMPTY)
    val state: StateFlow<DriverPrefenceState>
        get() = mutableState

    private fun getPrefrence() {
        updateState(isLoading = true)
        val token = sharedPref.invoke(Constants.TOKEN)
        val email = sharedPref.invoke(Constants.EMAIL)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase(token = token, email = email)) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = result.error.localizedMessage ?: Constants.UNEXPECTED_ERROR
                    )
                }
                is DomainModel.Success -> {
                    (result.data as PrefrenceData).apply {
                        updateState(
                            isLoading = false,
                            isSuccess = true,
                            acceptCash = this.acceptCash,
                            acceptTrips = this.acceptTrips,
                            excludeLowRated = this.excludeLowRated,
                            longDistance = this.longDistance,
                            preferredNavigation = this.preferredNavigation
                        )
                    }

                }
            }
        }
    }

    private fun setPrefrence() {
        updateState(isLoading = true)
        val data = mutableState.value.buildRequest()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = setUseCase(data)) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isPostSuccess = false,
                        errorPost = result.error.localizedMessage ?: Constants.UNEXPECTED_ERROR
                    )
                }
                is DomainModel.Success -> {
                    updateState(
                        isLoading = false,
                        isPostSuccess = true
                    )
                }
            }
        }
    }

    private fun DriverPrefenceState.buildRequest(): PrefrenceData =
        PrefrenceData(
            acceptCash = this.acceptCash,
            acceptTrips = this.acceptTrips,
            excludeLowRated = this.excludeLowRated,
            preferredNavigation = this.preferredNavigation,
            longDistance = this.longDistance
        )

    fun handleEvent(event: DriverPrefrenceEvent) {
        when (event) {
            DriverPrefrenceEvent.FetchPref -> getPrefrence()
            DriverPrefrenceEvent.UpdatePref -> setPrefrence()
        }
    }

    fun updateState(
        isLoading: Boolean? = null,
        isSuccess: Boolean? = null,
        acceptCash: Int? = null,
        acceptTrips: Int? = null,
        excludeLowRated: Int? = null,
        longDistance: Int? = null,
        preferredNavigation: String? = null,
        isPostSuccess: Boolean? = null,
        errorPost: String? = null,
        error: String? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                isLoading = isLoading ?: currentState.isLoading,
                isSuccess = isSuccess ?: currentState.isSuccess,
                acceptCash = acceptCash ?: currentState.acceptCash,
                acceptTrips = acceptTrips ?: currentState.acceptTrips,
                excludeLowRated = excludeLowRated ?: currentState.excludeLowRated,
                longDistance = longDistance ?: currentState.longDistance,
                preferredNavigation = preferredNavigation ?: currentState.preferredNavigation,
                isPostSuccess = isPostSuccess ?: currentState.isPostSuccess,
                errorPost = errorPost ?: currentState.errorPost,
                error = error ?: currentState.error

            )
        }
    }
}