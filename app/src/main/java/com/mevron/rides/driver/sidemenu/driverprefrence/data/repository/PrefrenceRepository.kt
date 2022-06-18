package com.mevron.rides.driver.sidemenu.driverprefrence.data.repository

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.GetPrefrenceModel
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.PrefrenceData
import com.mevron.rides.driver.sidemenu.driverprefrence.data.network.PrefrenceAPI
import com.mevron.rides.driver.sidemenu.driverprefrence.domain.model.PrefResponse
import com.mevron.rides.driver.sidemenu.driverprefrence.domain.model.PrefrenceDomainData
import com.mevron.rides.driver.sidemenu.driverprefrence.domain.repository.IPrefrenceRepository
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.GetSavedAddress
import com.mevron.rides.driver.sidemenu.savedplaces.domain.model.AddressDomainData
import com.mevron.rides.driver.sidemenu.savedplaces.domain.model.GetAddressDomainData
import com.mevron.rides.driver.sidemenu.savedplaces.domain.model.GetSavedAddressData

class PrefrenceRepository(private val api: PrefrenceAPI): IPrefrenceRepository {

    override suspend fun getdriverPref(email: String, token: String): DomainModel  = api.getPreference(email = email, token = token).let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("No saved address found"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    override suspend fun savedriverPref(data: PrefrenceData): DomainModel  = api.setPreference(data).let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("No saved address found"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    private fun GetPrefrenceModel.toDomainModel() = DomainModel.Success(
        data = this.success.getPrefrenceData.apply {
            PrefrenceDomainData(acceptCash = this.acceptCash, acceptTrips = this.acceptTrips, excludeLowRated = this.excludeLowRated, longDistance = this.longDistance, preferredNavigation = this.preferredNavigation ?: "")
        }
    )

    private fun GeneralResponse.toDomainModel() = DomainModel.Success(
        data = PrefResponse(message = this.success.message, status = this.success.status)
    )
}