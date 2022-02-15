package com.mevron.rides.driver.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mevron.rides.driver.auth.model.CreateAccountRequest
import com.mevron.rides.driver.auth.model.CreateResponse
import com.mevron.rides.driver.auth.model.OTPResponse
import com.mevron.rides.driver.auth.model.ValidateOTPRequest
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.remote.MevronRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountCreationViewModel @Inject constructor (private val repository: MevronRepo)  : ViewModel() {


    fun createAccount(data: CreateAccountRequest): LiveData<GenericStatus<CreateResponse>> {

        val result = MutableLiveData<GenericStatus<CreateResponse>>()

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = repository.createAccount(data)
                if(response.isSuccessful)
                    result.postValue(GenericStatus.Success(response.body()))
                else
                    result.postValue(GenericStatus.Error(HTTPErrorHandler.handleErrorWithCode(response)))
            }catch (ex: Exception){
                ex.printStackTrace()
                result.postValue(GenericStatus.Error(HTTPErrorHandler.httpFailWithCode(ex)))
            }
        }
        return result

    }
}