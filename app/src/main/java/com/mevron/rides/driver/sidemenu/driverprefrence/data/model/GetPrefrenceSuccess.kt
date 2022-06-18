package com.mevron.rides.driver.sidemenu.driverprefrence.data.model

import com.google.gson.annotations.SerializedName


data class GetPrefrenceSuccess(
    @SerializedName("data")
    val getPrefrenceData: PrefrenceData,
    val message: String,
    val status: String
)