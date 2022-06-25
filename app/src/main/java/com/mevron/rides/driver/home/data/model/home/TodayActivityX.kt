package com.mevron.rides.driver.home.data.model.home

data class TodayActivityX(
    val earnings: String,
    val online: String,
    val rides: Int,
    val trip_list: List<Trip>
)