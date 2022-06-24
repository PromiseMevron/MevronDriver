package com.mevron.rides.driver.home.data.model.home

import com.google.gson.annotations.SerializedName

data class HomeScreenContentData(
    val drive: Drive,
    val earnings: Earnings,
    @SerializedName("document_status")
    val documentStatus: Int,
    @SerializedName("online_status")
    val onlineStatus: Boolean,
)