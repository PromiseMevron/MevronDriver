package com.mevron.rides.driver.sidemenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.remote.MevronRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BankDetailViewModel @Inject constructor(private val repository: MevronRepo) : ViewModel() {

    fun deleteBank(id: String): LiveData<GenericStatus<GeneralResponse>> {

        val result = MutableLiveData<GenericStatus<GeneralResponse>>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.deleteBank(id)
                if (response.isSuccessful) {
                    result.postValue(GenericStatus.Success(response.body()))
                } else {
                    result.postValue(
                        GenericStatus.Error(
                            HTTPErrorHandler.handleErrorWithCode(
                                response
                            )
                        )
                    )
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                result.postValue(GenericStatus.Error(HTTPErrorHandler.httpFailWithCode(ex)))
            }
        }
        return result

    }

    fun updateBank(id: String): LiveData<GenericStatus<GeneralResponse>> {

        val result = MutableLiveData<GenericStatus<GeneralResponse>>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.updateBank(id)
                if (response.isSuccessful) {
                    result.postValue(GenericStatus.Success(response.body()))
                } else {
                    result.postValue(
                        GenericStatus.Error(
                            HTTPErrorHandler.handleErrorWithCode(
                                response
                            )
                        )
                    )
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                result.postValue(GenericStatus.Error(HTTPErrorHandler.httpFailWithCode(ex)))
            }
        }
        return result

    }
}