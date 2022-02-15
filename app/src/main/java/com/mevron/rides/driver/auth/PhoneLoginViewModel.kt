package com.mevron.rides.driver.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mevron.rides.driver.auth.model.RegisterBody
import com.mevron.rides.driver.auth.model.RegisterResponse
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.remote.MevronRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneLoginViewModel @Inject constructor (private val repository: MevronRepo)  : ViewModel() {

    fun phoneLogin(data: RegisterBody): LiveData<GenericStatus<RegisterResponse>> {

        val result = MutableLiveData<GenericStatus<RegisterResponse>>()

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = repository.registerPhone(data)
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