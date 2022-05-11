package com.mevron.rides.driver.home.data.model

import com.google.gson.annotations.SerializedName

data class SuccessData(
    @SerializedName("data")
    val contentData: HomeScreenContentData,
    @SerializedName("status")
    val status: String
)