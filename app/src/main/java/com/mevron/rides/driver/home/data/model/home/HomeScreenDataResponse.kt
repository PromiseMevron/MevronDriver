package com.mevron.rides.driver.home.data.model.home

import com.google.gson.annotations.SerializedName

data class HomeScreenDataResponse(
    @SerializedName("success")
    val successData: SuccessData
)