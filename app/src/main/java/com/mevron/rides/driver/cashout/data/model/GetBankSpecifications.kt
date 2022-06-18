package com.mevron.rides.driver.cashout.data.model

import com.google.gson.annotations.SerializedName

data class GetBankSpecifications(
    @SerializedName("success")
    val specificSuccess: SpecificSuccess
)