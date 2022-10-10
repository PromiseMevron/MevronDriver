package com.mevron.rides.driver.trips.data.repositroy

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.data.model.home.Trip
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.trips.data.model.alltrips.AllTripsResponse
import com.mevron.rides.driver.trips.data.model.alltrips.AllTripsResult
import com.mevron.rides.driver.trips.data.network.TripsAPI
import com.mevron.rides.driver.trips.domain.model.AllTripsDomainData
import com.mevron.rides.driver.trips.domain.model.GetTripDomainData
import com.mevron.rides.driver.trips.domain.repository.IGetTripsRepository
import com.mevron.rides.driver.util.Constants

class GetTripsRepository(private val api: TripsAPI) : IGetTripsRepository {

    override suspend fun getAllTrips(start: String, end: String): DomainModel =
        api.getAllTrips(startDate = start, endDate = end).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel() ?: DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(it)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        }


    private fun AllTripsResponse.toDomainModel() = DomainModel.Success(
        data = this.success.allTripsData.let {it1 ->
            GetTripDomainData(
                amount = "${it1.currency}${it1.earning}",
                endDate = it1.endDate,
                online = it1.online,
                rides = it1.rides,
                startDate = it1.startDate,
                results = it1.results.map {it ->
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