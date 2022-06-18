package com.mevron.rides.driver.home.trip_management.data.network

import com.mevron.rides.driver.home.trip_management.data.model.TripManagementResponse
import com.mevron.rides.driver.remote.TripManagementModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TripManagementAPI {

    @POST("api/v1/driver/auth/trip")
    suspend fun tripManagement(@Body data: TripManagementModel): Response<TripManagementResponse>
}