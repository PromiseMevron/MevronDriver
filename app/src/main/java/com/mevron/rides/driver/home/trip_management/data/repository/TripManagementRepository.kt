package com.mevron.rides.driver.home.trip_management.data.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.trip_management.data.network.TripManagementAPI
import com.mevron.rides.driver.home.trip_management.domain.model.TripManagementDomainModel
import com.mevron.rides.driver.home.trip_management.domain.repository.ITripManagementRepository
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.remote.TripManagementModel
import com.mevron.rides.driver.util.Constants

class TripManagementRepository(private val api: TripManagementAPI) : ITripManagementRepository {
    override suspend fun tripManagement(data: TripManagementModel): DomainModel =
        api.tripManagement(data = data).let {
            if (it.isSuccessful) {
                DomainModel.Success(data = TripManagementDomainModel("Operation was successful"))
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(it)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        }
}