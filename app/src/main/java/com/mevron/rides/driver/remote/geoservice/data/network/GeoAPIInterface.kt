package com.mevron.rides.driver.remote.geoservice.data.network

import com.mevron.rides.driver.remote.geoservice.data.model.GeoDirectionsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface GeoAPIInterface {
    @GET
    suspend fun getGeoDirections(@Url url: String): Response<GeoDirectionsResponse>

    @GET
     fun getGeoDirectionsCall(@Url url: String): Call<GeoDirectionsResponse>

}