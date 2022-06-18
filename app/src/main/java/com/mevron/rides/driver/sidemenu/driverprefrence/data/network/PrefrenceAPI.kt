package com.mevron.rides.driver.sidemenu.driverprefrence.data.network

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.GetPrefrenceModel
import com.mevron.rides.driver.sidemenu.driverprefrence.data.model.PrefrenceData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PrefrenceAPI {
    @POST("/api/v1/driver/auth/set-preference")
    suspend fun setPreference(@Body data: PrefrenceData): Response<GeneralResponse>

    @GET("/api/v1/driver/auth/get-preference")
    suspend fun getPreference(@Query("email") email: String, @Query("token") token: String): Response<GetPrefrenceModel>

}