package com.mevron.rides.driver.sidemenu.vehicle.data.network

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.sidemenu.vehicle.data.model.AllVehiclesResponse
import com.mevron.rides.driver.sidemenu.vehicle.data.model.vehicledetail.VehicleDetailResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VehicleApi {

    @GET("api/v1/driver/auth/vehicles")
    suspend fun getAllVehicles(): Response<AllVehiclesResponse>

    @GET("api/v1/driver/auth/vehicle/view/{uiid}")
    suspend fun getAVehicles(@Path("uiid") id: String): Response<VehicleDetailResponse>

    @DELETE("/api/v1/driver/auth/vehicle/remove/{uiid}")
    suspend fun deleteVehicles(@Path("uiid") id: String): Response<GeneralResponse>

    @POST("/api/v1/driver/auth/vehicle/status/{uiid}")
    suspend fun updateVehicles(@Path("uiid") id: String): Response<GeneralResponse>
}