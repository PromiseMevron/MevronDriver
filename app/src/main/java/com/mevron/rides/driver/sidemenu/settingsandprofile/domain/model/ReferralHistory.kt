package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model

data class ReferralHistory(
    val referralCode: String?,
    val referralStatus: Int,
    val referralData: List<ReferralData>
)

data class ReferralData(
    val category: String,
    val createAt: String,
    val description: String,
    val id: String,
    val title: String
)