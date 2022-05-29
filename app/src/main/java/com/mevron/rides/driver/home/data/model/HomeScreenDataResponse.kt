package com.mevron.rides.driver.home.data.model

import com.google.gson.annotations.SerializedName

data class HomeScreenDataResponse(
    @SerializedName("success")
    val successData: SuccessData
)