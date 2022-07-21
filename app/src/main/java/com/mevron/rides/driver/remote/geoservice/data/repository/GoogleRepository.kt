package com.mevron.rides.driver.remote.geoservice.data.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.remote.geoservice.data.model.GeoDirectionsResponse
import com.mevron.rides.driver.remote.geoservice.data.network.GeoAPIInterface
import com.mevron.rides.driver.remote.geoservice.domain.repository.IGoogleRepository

class GoogleRepository(private val api: GeoAPIInterface) : IGoogleRepository {

    override suspend fun makeGoogleCall(url: String): DomainModel =
        api.getGeoDirections(url = url).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Failed"))
            } else {
                DomainModel.Error(Throwable(it.errorBody().toString()))
            }
        }

    private fun GeoDirectionsResponse.toDomainModel() = DomainModel.Success(
        data = this
    )
}