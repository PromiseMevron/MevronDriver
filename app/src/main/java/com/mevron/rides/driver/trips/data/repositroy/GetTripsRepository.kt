package com.mevron.rides.driver.trips.data.repositroy

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.data.model.home.Trip
import com.mevron.rides.driver.trips.data.model.alltrips.AllTripsResponse
import com.mevron.rides.driver.trips.data.model.alltrips.AllTripsResult
import com.mevron.rides.driver.trips.data.network.TripsAPI
import com.mevron.rides.driver.trips.domain.model.AllTripsDomainData
import com.mevron.rides.driver.trips.domain.model.GetTripDomainData
import com.mevron.rides.driver.trips.domain.repository.IGetTripsRepository

class GetTripsRepository(private val api: TripsAPI) : IGetTripsRepository {

    override suspend fun getAllTrips(start: String, end: String): DomainModel =
        api.getAllTrips(startDate = start, endDate = end).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Trips not found"))
            } else {
                DomainModel.Error(Throwable(it.errorBody().toString()))
            }
        }


    private fun AllTripsResponse.toDomainModel() = DomainModel.Success(
        data = this.success.allTripsData.apply {
            GetTripDomainData(
                amount = "${this.currency}${this.earning}",
                endDate = endDate,
                online = online,
                rides = rides,
                startDate = startDate,
                results = results.map {
                    AllTripsResult(
                        amount = it.amount,
                        time = it.time,
                     date =it.date,
                        id = it.id,
                    status = it.status,
                    title = it.title

                    )
                })
        }

    )
}