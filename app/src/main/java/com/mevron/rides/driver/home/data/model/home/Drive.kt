package com.mevron.rides.driver.home.data.model.home

import com.google.gson.annotations.SerializedName

data class Drive(
    val learn: List<Any>,
    @SerializedName("scheduled_pickups")
    val scheduledPickups: List<ScheduledPickup>,
    @SerializedName("scheduled_pickups_count")
    val scheduledPickupsCount: Int,
    @SerializedName("today_activity")
    val todayActivity: TodayActivity,
    @SerializedName("weekly_challenges")
    val weeklyChallenges: List<WeeklyChallenge>
)