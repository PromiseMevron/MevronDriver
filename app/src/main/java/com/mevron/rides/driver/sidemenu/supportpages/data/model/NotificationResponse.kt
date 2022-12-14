package com.mevron.rides.driver.sidemenu.supportpages.data.model

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    val success: NotificationSuccess
)

data class NotificationSuccess(
    @SerializedName("data")
    val notificationData: NotificationData,
    val message: String,
    val status: String
)

data class NotificationData(
    val current_page: Int,
    val from: Int,
    val last_page: Int,
    val per_page: Int,
    val result: List<NotificationBody>,
    val to: Int
)

data class NotificationBody(
    val id: Int,
    val title:String,
    val description:String,
    val createAt: String
)