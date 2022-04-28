package com.mevron.rides.driver.updateprofile.data.network

import com.mevron.rides.driver.updateprofile.data.model.CarMakesResponse
import com.mevron.rides.driver.updateprofile.data.model.CarModelResponse
import com.mevron.rides.driver.updateprofile.data.model.CarYearResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CarPropertiesApi {

    @GET("api/v1/driver/auth/get-car-list")
    suspend fun getCarMakes(): Response<CarMakesResponse>

    @GET("api/v1/driver/auth/get-car-model")
    suspend fun getCarModels(@QueryMap params: HashMap<String, String>): Response<CarModelResponse>

    @GET("api/v1/driver/auth/get-car-year")
    suspend fun getCarYear(@QueryMap params: HashMap<String, String>): Response<CarYearResponse>
}