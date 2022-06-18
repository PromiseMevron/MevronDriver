package com.mevron.rides.driver.trips.data.network

import com.mevron.rides.driver.trips.data.model.alltrips.AllTripsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TripsAPI {

    @GET("api/v1/driver/auth/trips")
    suspend fun getAllTrips(@Query("startDate") startDate: String, @Query("endDate") endDate: String): Response<AllTripsResponse>

}