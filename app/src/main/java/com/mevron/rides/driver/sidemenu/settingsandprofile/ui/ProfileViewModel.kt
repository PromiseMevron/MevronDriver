package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.authentication.domain.usecase.GetSharedPreferenceUseCase
import com.mevron.rides.driver.authentication.domain.usecase.SetPreferenceUseCase
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.remote.MevronRepo
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.GetProfileData
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.SaveDetailsRequest
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.usecase.GetProfileUseCase
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.usecase.UpdateProfileUseCase
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.event.SettingsProfileEvent
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.state.SettingsProfileState
import com.mevron.rides.driver.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCase: GetProfileUseCase,
    private val updateCase: UpdateProfileUseCase,
    private val useSharedCase: GetSharedPreferenceUseCase,
    private val setSharedCase: SetPreferenceUseCase,
    private val repository: MevronRepo
) : ViewModel() {

    private val mutableState: MutableStateFlow<SettingsProfileState> =
        MutableStateFlow(SettingsProfileState.EMPTY)

    val state: StateFlow<SettingsProfileState>
        get() = mutableState

    private fun getProfile() {

        val gson = Gson()
        val json = useSharedCase(Constants.PROFILE)
        json.let {
            val user = gson.fromJson(it, GetProfileData::class.java)
            updateState(profile = user)
        }

        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = useCase()) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = ""
                    )
                }
                is DomainModel.Success -> {
                    val data = result.data as GetProfileData
                    val user = gson.toJson(data)
                    setSharedCase(Constants.PROFILE, user)
                    updateState(
                        isLoading = false,
                        isRequestSuccess = true,
                        profile = data
                    )
                }
            }
        }
    }

    fun uploadProfile(data: MultipartBody.Part): LiveData<GenericStatus<GeneralResponse>> {
        val result = MutableLiveData<GenericStatus<GeneralResponse>>()
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = repository.uploadProfile(data)
                if(response.isSuccessful)
                    result.postValue(GenericStatus.Success(response.body()))
                else
                    result.postValue(GenericStatus.Error(HTTPErrorHandler.handleErrorWithCode(response)))
            }catch (ex: Exception){
                ex.printStackTrace()
                result.postValue(GenericStatus.Error(HTTPErrorHandler.httpFailWithCode(ex)))
            }
        }

        return  result
    }

    private fun updateProfile() {
        val data = mutableState.value.toBuildRequest()
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = updateCase(data)) {
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isSuccess = false,
                        error = ""
                    )
                }
                is DomainModel.Success -> {
                    updateState(
                        isLoading = false,
                        isRequestSuccess = true,
                    )
                }
            }
        }
    }

    private fun SettingsProfileState.toBuildRequest(): SaveDetailsRequest {
        this.profile.apply {
            return SaveDetailsRequest(
                email = email ?: "",
                firstName = firstName ?: "",
                lastName = lastName ?: "",
                phoneNumber = phoneNumber
            )
        }

    }

    fun handleEvent(event: SettingsProfileEvent) {
        when (event) {
            SettingsProfileEvent.FetchFromApi -> getProfile()
            SettingsProfileEvent.UpdateProfile -> updateProfile()
            else -> {}
        }
    }

    fun updateState(
        isLoading: Boolean? = null,
        isRequestSuccess: Boolean? = null,
        error: String? = null,
        profile: GetProfileData? = null
    ) {
        val currentState = mutableState.value
        mutableState.update {
            currentState.copy(
                isLoading = isLoading ?: currentState.isLoading,
                isSuccess = isRequestSuccess ?: currentState.isSuccess,
                error = error ?: currentState.error,
                profile = profile ?: currentState.profile
            )
        }
    }
}