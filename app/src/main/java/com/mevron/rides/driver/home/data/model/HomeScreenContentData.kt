package com.mevron.rides.driver.home.data.model

import com.google.gson.annotations.SerializedName

data class HomeScreenContentData(
    @SerializedName("document_status")
    val documentStatus: Int,
    @SerializedName("earnings")
    val earnings: Int,
    @SerializedName("online_status")
    val onlineStatus: Boolean,
    @SerializedName("rides")
    val rides: Int,
    @SerializedName("scheduled_pickups")
    val scheduledPickUps: List<Any>,
    @SerializedName("weekly_challenges")
    val weeklyChallenges: List<Any>
)