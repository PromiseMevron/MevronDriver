package com.mevron.rides.driver.home.data.model.home

import com.google.gson.annotations.SerializedName

data class Earnings(
    val balance: String,
    val currency: String,
    @SerializedName("currency_symbol")
    val currencySymbol: String,
    @SerializedName("earning_goal")
    val earningGoal: EarningGoal,
    val nextPaymentDate: String,
    @SerializedName("today_activity")
    val todayActivity: TodayActivityX,
    @SerializedName("weekly_summary")
    val weeklySummary: WeeklySummary
)