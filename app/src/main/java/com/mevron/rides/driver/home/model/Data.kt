package com.mevron.rides.driver.home.model

data class Data(
    val document_status: Int,
    val earnings: Int,
    val online_status: Boolean,
    val rides: Int,
    val scheduled_pickups: List<Any>,
    val weekly_challenges: List<Any>
)