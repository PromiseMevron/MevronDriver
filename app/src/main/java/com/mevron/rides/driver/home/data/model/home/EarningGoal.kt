package com.mevron.rides.driver.home.data.model.home

import com.google.gson.annotations.SerializedName

data class EarningGoal(
    val earned_goal: String,
    @SerializedName("expiry_date")
    val expiryDate: String,
    val percentage: String,
    @SerializedName("weekly_goal")
    val weeklyGoal: String
)