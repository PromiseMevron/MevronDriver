package com.mevron.rides.driver.sidemenu.settingsandprofile.data.model

data class GetReferalHistory(
    val success: Success
)
data class Success(
    val `data`: Data,
    val message: String,
    val status: String
)

data class Data(
    val current_page: Int,
    val from: Int,
    val last_page: Int,
    val per_page: Int,
    val referralCode: String?,
    val referralStatus: Int,
    val result: List<Result>,
    val to: Int
)

data class Result(
    val category: String,
    val createAt: String,
    val description: String,
    val id: String,
    val title: String
)