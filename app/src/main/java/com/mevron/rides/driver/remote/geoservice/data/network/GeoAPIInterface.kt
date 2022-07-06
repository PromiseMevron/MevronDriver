package com.mevron.rides.driver.remote.geoservice.data.network

import com.mevron.rides.driver.remote.geoservice.data.model.GeoDirectionsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GeoAPIInterface {
    @GET
    fun getGeoDirections(@Url url: String): Call<GeoDirectionsResponse>
}