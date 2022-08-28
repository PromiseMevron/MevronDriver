package com.mevron.rides.driver.remote

import androidx.lifecycle.LiveData
import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.auth.model.OTPResponse
import com.mevron.rides.driver.auth.model.caryear.GetCarYear
import com.mevron.rides.driver.auth.model.getcar.GetCallsResponse
import com.mevron.rides.driver.auth.model.getmodel.GetModelResponse
import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.home.model.HomeScreenResponse
import com.mevron.rides.driver.home.model.documents.DocumentStatusResponse
import com.mevron.rides.driver.localdb.MevronDao
import com.mevron.rides.driver.localdb.SavedAddress
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.GetSavedAddress
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.SaveAddressRequest
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.UpdateAddress
import com.mevron.rides.driver.remote.model.getcard.AddCard
import com.mevron.rides.driver.remote.model.getcard.GetCardResponse
import com.mevron.rides.driver.sidemenu.emerg.data.model.AddContactRequest
import com.mevron.rides.driver.sidemenu.emerg.data.model.GetContactsResponse
import com.mevron.rides.driver.sidemenu.emerg.data.model.UpdateEmergencyContact
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.GetPrefrenceModel
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.PrefrenceData
import com.mevron.rides.driver.sidemenu.supportpages.data.model.NotificationResponse
import com.mevron.rides.driver.updateprofile.domain.model.AddVehicleRequest
import com.mevron.rides.driver.updateprofile.domain.model.SecurityNumRequest
import com.mevron.rides.driver.util.Constants
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class MevronRepo @Inject constructor ( private val api: MevronAPI, private val dao: MevronDao) {

    suspend fun validateOTP(data: VerifyOTPRequest): Response<OTPResponse> {
        return api.verifyOTP(data)

    }


    suspend fun createAccount(data: CreateAccountRequest): Response<GeneralResponse> {
        return api.createAccount(data)
    }

    suspend fun addVehicle(data: AddVehicleRequest): Response<GeneralResponse> {
        return api.addVehicle(data)
    }

    suspend fun uploadLicence(data: MultipartBody.Part): Response<GeneralResponse>{
        return api.uploadLicence(data)
    }

    suspend fun uploadProfile(data: MultipartBody.Part): Response<GeneralResponse>{
        return api.uploadProfile(data)
    }


    suspend fun uploadSticker(data: MultipartBody.Part): Response<GeneralResponse>{
        return api.uploadSticker(data)
    }

    suspend fun uploadInsurance(data: MultipartBody.Part): Response<GeneralResponse>{
        return api.uploadInsurance(data)
    }

    suspend fun uploadSecurityNum(data: SecurityNumRequest): Response<GeneralResponse> {
        return api.uploadSecurityNumber(data)
    }

    suspend fun getCarsMake(): Response<GetCallsResponse>{
        return api.getCarMakes()
    }

    suspend fun getCarsModel(param: HashMap<String, String>): Response<GetModelResponse>{
        return api.getCarModels(param)
    }

    suspend fun getCarsYear(param: HashMap<String, String>): Response<GetCarYear>{
        return api.getCarYear(param)
    }

    suspend fun tripManagement(data: TripManagementModel): Response<GeneralResponse>{
        return api.tripManagement(data)
    }

    suspend fun toggleStatus(): Response<GeneralResponse>{
        return  api.toggleStatus()
    }


    suspend fun getHomeStatus(): Response<HomeScreenResponse>{
        return api.getHomeStatus()
    }

    suspend fun getDocumentStatus(): Response<DocumentStatusResponse>{
        return  api.getDocumentStatus()
    }

    suspend fun saveAddress(data: SaveAddressRequest): Response<GeneralResponse> {
        return api.saveAddress(data)
    }

    suspend fun getAddress(): Response<GetSavedAddress> {
        return api.getAddress()
    }

    suspend fun saveAddressInDb(add: SavedAddress) {
      //  dao.insert(add)
    }

    suspend fun deleteAllAdd() {
       // dao.deleteAllAddress()
    }

    suspend fun updataAddInDB(add: SavedAddress) {
      //  dao.update(add)
    }

    suspend fun updataAdd(path: String, add: UpdateAddress): Response<GeneralResponse> {
        return api.updateAddress(path, add)
    }

   /* fun getllAddress(): LiveData<MutableList<SavedAddress>> {
        return dao.getAllAddress()
    }*/


    suspend fun saveEmergency(data: AddContactRequest): Response<GeneralResponse> {
        return api.saveEmergency(data)
    }



    suspend fun getEmergency(): Response<GetContactsResponse> {
        return api.getEmergency()
    }

    suspend fun updateEmergency(data: UpdateEmergencyContact, id: String): Response<GeneralResponse> {
        return api.updateEmergency(id = id, data = data)
    }

    suspend fun deleteEmergency(id: String): Response<GeneralResponse> {
        return api.deleteEmergency(id = id)
    }

    suspend fun getPromo(): Response<GeneralResponse> {
        return api.getPromo()
    }


    suspend fun getNotification(): Response<NotificationResponse> {
        return api.getNotifications()
    }


    suspend fun getAllVehicle(): Response<GeneralResponse> {
        return api.getAllVehicles()
    }


    suspend fun getAVehicle(id: String): Response<GeneralResponse> {
        return api.getAVehicles(id)
    }


    suspend fun deleteVehicle(id: String): Response<GeneralResponse> {
        return api.deleteVehicles(id)
    }

    suspend fun getCards(): Response<GetCardResponse> {
        return api.getCards()
    }

    suspend fun addCard(data: AddCard): Response<GeneralResponse> {
        return api.addCard(data)
    }

    suspend fun deleteCard(id: String): Response<GeneralResponse> {
        return api.deleteCard(id)
    }

    suspend fun deleteBank(id: String): Response<GeneralResponse> {
        return api.deleteBank(id)
    }

    suspend fun setPreference(data: PrefrenceData): Response<GeneralResponse> {
        return api.setPreference(data)
    }

    suspend fun getPreference(email: String, token: String): Response<GetPrefrenceModel> {
        return api.getPreference(email = email, token = token)
    }


    suspend fun getAllTrips(startDate: String, endDate: String): Response<GeneralResponse>{
        return api.getAllTrips(startDate = startDate, endDate = endDate)
    }

}