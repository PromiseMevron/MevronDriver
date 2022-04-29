package com.mevron.rides.driver.updateprofile.data.model

import com.google.gson.annotations.SerializedName

data class CarYearResponse(
    @SerializedName("success")
    val carYearSuccessData: CarYearSuccessResponseData
)

data class CarYearSuccessResponseData(
    @SerializedName("data")
    val carYearDataList: List<CarYearData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class CarYearData(
    @SerializedName("Year")
    val year: String,
    @SerializedName("id")
    val id: Int
)
