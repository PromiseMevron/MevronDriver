package com.mevron.rides.driver.sidemenu.savedplaces.domain.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.SaveAddressRequest
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.UpdateAddress

interface IAddressRepository {
    suspend fun getSavedAddresses(): DomainModel
    suspend fun addAnAddress(data: SaveAddressRequest): DomainModel
    suspend fun updateAnAddress(identifier: String, data: UpdateAddress): DomainModel
}
