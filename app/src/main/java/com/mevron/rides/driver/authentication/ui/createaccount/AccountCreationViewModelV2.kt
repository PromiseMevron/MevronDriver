package com.mevron.rides.driver.authentication.ui.createaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mevron.rides.driver.authentication.data.models.createaccount.GetCityRequest
import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.model.GetCitiesData
import com.mevron.rides.driver.authentication.domain.model.GetCityDomainData
import com.mevron.rides.driver.authentication.domain.usecase.CreateAccountUseCase
import com.mevron.rides.driver.authentication.domain.usecase.GetCityUseCase
import com.mevron.rides.driver.authentication.domain.usecase.SetPreferenceUseCase
import com.mevron.rides.driver.authentication.ui.createaccount.event.CreateAccountEvent
import com.mevron.rides.driver.authentication.ui.createaccount.state.CreateAccountState
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.domain.update
import com.mevron.rides.driver.util.Constants
import com.mevron.rides.driver.util.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AccountCreationViewModelV2 @Inject constructor(
    private val useCase: CreateAccountUseCase,
    private val setPreferenceUseCase: SetPreferenceUseCase,
    private val getCitiesData: GetCityUseCase
) : ViewModel() {

    private val mutableState: MutableStateFlow<CreateAccountState> =
        MutableStateFlow(CreateAccountState.EMPTY)

    val state: StateFlow<CreateAccountState>
        get() = mutableState

    fun handleEvent(event: CreateAccountEvent) {
        when(event){
            CreateAccountEvent.OnCreateAccountClick -> createAccount(mutableState.value.buildRequest())
        }
    }

    private fun createAccount(request: CreateAccountRequest) {
        updateState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO)  {
            when (val result = useCase(request)){
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                        isLoading = false,
                        isRequestSuccess = false,
                        error = result.error.message ?: Constants.UNEXPECTED_ERROR
                    )
                }
                is DomainModel.Success -> {
                    setPreferenceUseCase(Constants.EMAIL, mutableState.value.email)
                    updateState(
                        isLoading = false,
                        isRequestSuccess = true,
                    )
                }
            }
        }
    }

     fun getCities() {
        viewModelScope.launch(Dispatchers.IO)  {
            when (val result = getCitiesData(GetCityRequest(countryName = state.value.country))){
                is DomainModel.Error -> mutableState.update {
                    mutableState.value.copy(
                    )
                }
                is DomainModel.Success -> {
                    val data = result.data as GetCityDomainData
                    updateState(
                       cityData = data.cities
                    )
                }
            }
        }
    }

    private fun CreateAccountState.buildRequest(): CreateAccountRequest =
        CreateAccountRequest(
            city = city,
            email = email,
            firstName = firstName,
            lastName = lastName,
            referralCode = referral,
            type = type
        )


    private fun DomainModel.Error.buildString(): String =
        this.error.localizedMessage ?: "Registration failed"

    fun updateState(
         phoneNumber: String? = null,
         name: String? = null,
         fName: String? = null,
         lName: String? = null,
         city: String? = null,
         cityName: String? = null,
         referral: String? = null,
         isLoading: Boolean? = false,
         isRequestSuccess: Boolean? = false,
         email: String? = null,
         error: String? = null,
         country: String? = null,
         detailsComplete: Boolean? = null,
         cityData: List<GetCitiesData>? = null,
         type: String? = null
    ) {
        val currentValue = mutableState.value
      /*  val fullName = name?.split(" ")
        val fName = fullName?.get(0)
        var lName: String? = ""
        fullName?.size?.let {
            for (i in 1 until (it)){
                lName += fullName[i]
            }
        }
        if (lName.isNullOrEmpty()){
            lName = null
        }*/

        mutableState.update {
            currentValue.copy(
                phoneNumber = phoneNumber ?: currentValue.phoneNumber,
                firstName = fName ?: currentValue.firstName,
                lastName = lName ?: currentValue.lastName,
                city = city ?: currentValue.city,
                isRequestSuccess = isRequestSuccess ?: currentValue.isRequestSuccess,
                isLoading = isLoading ?: currentValue.isLoading,
                referral = referral ?: currentValue.referral,
                error = error?: currentValue.error,
                email = email?: currentValue.email,
                detailsComplete = detailsComplete ?: currentValue.detailsComplete,
                validEmail = email?.isValidEmail() ?: currentValue.validEmail,
                country = country ?: currentValue.country,
                cityData = cityData ?: currentValue.cityData,
                cityName = cityName ?: currentValue.cityName,
                type = type ?: currentValue.type
            )
        }
    }
}