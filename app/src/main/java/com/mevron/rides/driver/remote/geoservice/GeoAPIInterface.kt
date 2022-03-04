package com.mevron.rides.driver.remote.geoservice

import com.mevron.rides.driver.util.GeoDirectionsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface GeoAPIInterface {

    @GET
    fun getGeoDirections(@Url url: String): Call<GeoDirectionsResponse>
}