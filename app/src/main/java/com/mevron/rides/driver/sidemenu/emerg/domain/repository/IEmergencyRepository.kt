package com.mevron.rides.driver.sidemenu.emerg.domain.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.sidemenu.emerg.data.model.AddContactRequest
import com.mevron.rides.driver.sidemenu.emerg.data.model.UpdateEmergencyContact

interface IEmergencyRepository {
    suspend fun getEmergencyContact(): DomainModel
    suspend fun updateEmergencyContact(
        id: String,
        data: UpdateEmergencyContact
    ): DomainModel
    suspend fun saveEmergencyContact(data: AddContactRequest): DomainModel
    suspend fun deleEmergencyContact(id: String): DomainModel
}