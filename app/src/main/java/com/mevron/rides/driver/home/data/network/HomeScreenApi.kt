package com.mevron.rides.driver.home.data.network

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.home.data.model.HomeScreenContentData
import com.mevron.rides.driver.home.data.model.HomeScreenDataResponse
import com.mevron.rides.driver.home.model.HomeScreenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface  HomeScreenApi {
    @POST("api/v1/driver/auth/toggle-online-status")
    suspend fun toggleStatus(): Response<GeneralResponse>

    @GET("api/v1/driver/auth/homescreen")
    suspend fun getHomeStatus(): Response<HomeScreenDataResponse>
}