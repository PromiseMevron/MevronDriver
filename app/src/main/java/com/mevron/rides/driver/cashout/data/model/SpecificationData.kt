package com.mevron.rides.driver.cashout.data.model

import com.google.gson.annotations.SerializedName

data class SpecificationData(
    @SerializedName("data")
    val infoData: List<BankUpdateInfo>
)