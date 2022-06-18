package com.mevron.rides.driver.sidemenu.supportpages.data.network

import com.mevron.rides.driver.sidemenu.supportpages.data.model.NotificationResponse
import retrofit2.Response
import retrofit2.http.GET

interface SupportAPI {

    @GET("/api/v1/driver/auth/notifications?page=1")
    suspend fun getNotifications(): Response<NotificationResponse>
}