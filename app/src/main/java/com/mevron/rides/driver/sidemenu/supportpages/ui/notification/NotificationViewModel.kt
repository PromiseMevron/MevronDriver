package com.mevron.rides.driver.sidemenu.supportpages.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.sidemenu.supportpages.domain.model.SupportDomainData
import com.mevron.rides.driver.sidemenu.supportpages.domain.model.Supports
import com.mevron.rides.driver.sidemenu.supportpages.domain.usecase.GetNotificationUseCase
import com.mevron.rides.driver.sidemenu.supportpages.ui.notification.event.NotificationEvents
import com.mevron.rides.driver.sidemenu.supportpages.ui.notification.state.NotificationState
import com.mevron.rides.driver.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val useCase: GetNotificationUseCase) :
    ViewModel() {

    private val mutableState: MutableStateFlow<NotificationState> =
        MutableStateFlow(NotificationState.EMPTY)

    val state: StateFlow<NotificationState>
        get() = mutableState

    private fun getNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase()) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = result.error.localizedMessage ?: Constants.UNEXPECTED_ERROR
                    )
                }
                is DomainModel.Success -> {
                    val data = result.data as SupportDomainData
                    updateState(
                        isLoading = false,
                        isRequestSuccess = true,
                        notification = data.notifications.toMutableList()
                    )
                }
            }
        }
    }

    fun handleEvent(event: NotificationEvents) {
        when (event) {
            is NotificationEvents.GetNotifications ->
                getNotifications()
            is NotificationEvents.OnBackButtonClicked ->
                updateState(backButton = true)
        }
    }

    fun updateState(
        isLoading: Boolean? = null,
        isRequestSuccess: Boolean? = null,
        notification: MutableList<Supports>? = null,
        backButton: Boolean? = null,
        error: String? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                isLoading = isLoading ?: currentState.isLoading,
                isSuccess = isRequestSuccess ?: currentState.isSuccess,
                data = notification ?: currentState.data,
                backButton = backButton ?: currentState.backButton,
                error = error ?: currentState.error
            )
        }
    }
}