package com.mevron.rides.driver.sidemenu.savedplaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mevron.rides.driver.localdb.SavedAddress
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.remote.MevronRepo
import com.mevron.rides.driver.remote.model.GetSavedAddresss
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSavedPlaceViewModel @Inject constructor (private val repository: MevronRepo) : ViewModel() {

    fun getAddressFromDB(): LiveData<MutableList<SavedAddress>> {
        return repository.getllAddress()
    }

    fun getAddresses(): LiveData<GenericStatus<GetSavedAddresss>> {

        val result = MutableLiveData<GenericStatus<GetSavedAddresss>>()

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = repository.getAddress()
                if(response.isSuccessful){
                    result.postValue(GenericStatus.Success(response.body()))
                    val dt = response.body()?.success?.data
                    if (dt?.isNotEmpty() == true){
                        repository.deleteAllAdd()
                        for (d in dt){
                            val add = SavedAddress(type = d.type, name = d.name, lat = d.latitude,
                                lng = d.longitude, address = d.address, uiid = d.uuid
                            )
                            repository.saveAddressInDb(add)
                        }
                    }
                }
                else{
                    result.postValue(GenericStatus.Error(HTTPErrorHandler.handleErrorWithCode(response)))
                }
            }catch (ex: Exception){
                ex.printStackTrace()
                result.postValue(GenericStatus.Error(HTTPErrorHandler.httpFailWithCode(ex)))
            }
        }
        return result

    }
}