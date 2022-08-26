package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.authentication.domain.usecase.GetSharedPreferenceUseCase
import com.mevron.rides.driver.authentication.domain.usecase.SetPreferenceUseCase
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SetReferal
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.ReferralData
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.ReferralHistory
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.usecase.GetReferalHistoryUseCase
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.usecase.SetReferralUseCase
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.event.ReferalEvent
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.state.ReferralState
import com.mevron.rides.driver.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReferralViewModel @Inject constructor(
    private val useCase: GetReferalHistoryUseCase,
    private val setCase: SetReferralUseCase,
    private val getPrefUseCase: GetSharedPreferenceUseCase,
    private val setPrefUseCase: SetPreferenceUseCase
) : ViewModel() {
    private val mutableState: MutableStateFlow<ReferralState> =
        MutableStateFlow(ReferralState.EMPTY)

    val state: StateFlow<ReferralState>
        get() = mutableState

    fun handleEvent(event: ReferalEvent) {
        when (event) {
            ReferalEvent.GetReferalDetail -> getReferal()
            ReferalEvent.SetReferalDetail -> addReferal()
            ReferalEvent.GetReferalPrefDetail -> fetchFromPref()
        }
    }

    private fun fetchFromPref() {
        updateState(
            referralCode = getPrefUseCase(Constants.REFERRAL),
            referralStatus = (getPrefUseCase(Constants.REFERRAL_STATUS)).toInt()
        )
    }

    private fun getReferal() {
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase()) {
                is DomainModel.Success -> {
                    val data = result.data as ReferralHistory
                    updateState(
                        isLoading = false,
                        referralStatus = data.referralStatus,
                        referralCode = data.referralCode,
                        data = data.referralData
                    )
                    setPrefUseCase(Constants.REFERRAL, data.referralCode ?: "")
                    setPrefUseCase(Constants.REFERRAL_STATUS, (data.referralStatus ?: 0).toString())
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        error = "Failure to fetch referral history"
                    )
                }
            }
        }
    }


    private fun addReferal() {
        val data = toData()
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = setCase(data)) {
                is DomainModel.Success -> {
                    val response = result.data
                    updateState(
                        isLoading = false,
                        setReferal = false,
                        succes = true
                    )
                    getReferal()
                }
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        error = "Failure to set referral"
                    )
                }
            }
        }
    }

    private fun toData(): SetReferal {
        return SetReferal(mutableState.value.setCode)
    }

    fun updateState(
        isLoading: Boolean? = null,
        data: List<ReferralData>? = null,
        setReferal: Boolean? = null,
        numberOfRides: String? = null,
        referalID: String? = null,
        referralStatus: Int? = null,
        referralCode: String? = null,
        setCode: String? = null,
        error: String? = null,
        succes: Boolean? = null
    ) {
        val currentValue = mutableState.value
        mutableState.update {
            currentValue.copy(
                isLoading = isLoading ?: currentValue.isLoading,
                refData = data ?: currentValue.refData,
                setCode = setCode ?: currentValue.setCode,
                setReferal = setReferal ?: currentValue.setReferal,
                numberOfRides = numberOfRides ?: currentValue.numberOfRides,
                referalID = referalID ?: currentValue.referalID,
                referralCode = referralCode ?: currentValue.referralCode,
                referralStatus = referralStatus ?: currentValue.referralStatus,
                error = error ?: currentValue.error,
                succes = succes ?: currentValue.succes
            )
        }
    }

}