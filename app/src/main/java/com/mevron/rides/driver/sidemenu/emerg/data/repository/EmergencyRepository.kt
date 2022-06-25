package com.mevron.rides.driver.sidemenu.emerg.data.repository

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.sidemenu.emerg.data.model.AddContactRequest
import com.mevron.rides.driver.sidemenu.emerg.data.model.GetContactsResponse
import com.mevron.rides.driver.sidemenu.emerg.data.model.UpdateEmergencyContact
import com.mevron.rides.driver.sidemenu.emerg.data.network.EmergencyAPI
import com.mevron.rides.driver.sidemenu.emerg.domain.model.ContactResponse
import com.mevron.rides.driver.sidemenu.emerg.domain.model.GetContactDomain
import com.mevron.rides.driver.sidemenu.emerg.domain.model.GetContactDomainData
import com.mevron.rides.driver.sidemenu.emerg.domain.repository.IEmergencyRepository

class EmergencyRepository(private val api: EmergencyAPI) : IEmergencyRepository {

    override suspend fun getEmergencyContact() = api.getEmergency().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("No Contact Saved"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    override suspend fun updateEmergencyContact(
        id: String,
        data: UpdateEmergencyContact
    ): DomainModel = api.updateEmergency(id, data).let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Failure to update contact"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    override suspend fun saveEmergencyContact(data: AddContactRequest) =
        api.saveEmergency(data).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel()
                    ?: DomainModel.Error(Throwable("Failure to save contact"))
            } else {
                DomainModel.Error(Throwable(it.errorBody().toString()))
            }
        }

    override suspend fun deleEmergencyContact(id: String) = api.deleteEmergency(id).let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Failure to save contact"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    private fun GetContactsResponse.toDomainModel() = DomainModel.Success(
        data = if (this.getContactSuccess.getContactData == null){
            GetContactDomain(savedAddresses = mutableListOf())
        }else{
            GetContactDomain(
                savedAddresses = this.getContactSuccess.getContactData.map {
                    GetContactDomainData(id = it.id, name = it.name, phone = it.phoneNumber)
                }
            )
        }

    )

    private fun GeneralResponse.toDomainModel() = DomainModel.Success(
        data = ContactResponse(
            message = this.success.message, status = this.success.status
        )
    )
}