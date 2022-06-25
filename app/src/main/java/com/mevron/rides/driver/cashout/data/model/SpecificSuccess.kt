package com.mevron.rides.driver.cashout.data.model

import com.google.gson.annotations.SerializedName

data class SpecificSuccess(
    @SerializedName("data")
    val specdata: SpecificationData,
    val message: String,
    val status: String
)