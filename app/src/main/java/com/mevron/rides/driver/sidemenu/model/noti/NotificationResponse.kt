package com.mevron.rides.driver.sidemenu.model

data class NotificationResponse(
    val success: Success1
)

data class Success1(
    val `data`: Data1,
    val message: String,
    val status: String
)

data class Data1(
    val current_page: Int,
    val from: Int,
    val last_page: Int,
    val per_page: Int,
    val result: List<Any>,
    val to: Int
)