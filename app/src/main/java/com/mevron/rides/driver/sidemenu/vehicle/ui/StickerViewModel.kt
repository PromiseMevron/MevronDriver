package com.mevron.rides.driver.sidemenu.vehicle.ui

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
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class StickerViewModel @Inject constructor (private val repository: MevronRepo)  : ViewModel() {

    fun uploadSticker(data: MultipartBody.Part, id: String): LiveData<GenericStatus<GeneralResponse>> {
        val result = MutableLiveData<GenericStatus<GeneralResponse>>()
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = repository.uploadSticker(data, id)
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
}