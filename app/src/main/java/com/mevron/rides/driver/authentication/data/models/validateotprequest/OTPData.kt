package com.mevron.rides.driver.authentication.data.models.validateotprequest

import com.google.gson.annotations.SerializedName

data class OTPData(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("riderType")
    val riderType: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("uuid")
    val uuid: String
)