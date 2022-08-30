package com.mevron.rides.driver.home.data.network

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.home.data.model.home.DeviceID
import com.mevron.rides.driver.home.data.model.home.HomeScreenDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeScreenApi {
    @POST("api/v1/driver/auth/toggle-online-status")
    suspend fun toggleStatus(): Response<GeneralResponse>

    @GET("api/v1/driver/auth/homescreen")
    suspend fun getHomeStatus(): Response<HomeScreenDataResponse>

    @POST("api/v1/driver/auth/update-device-id")
    suspend fun updateToken(@Body id: DeviceID): Response<GeneralResponse>
}