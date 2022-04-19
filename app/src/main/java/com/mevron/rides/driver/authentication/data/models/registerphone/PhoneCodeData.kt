package com.mevron.rides.driver.authentication.data.models.registerphone

import com.google.gson.annotations.SerializedName

data class PhoneCodeData(
    @SerializedName("code")
    val code: String,
    @SerializedName("expireAt")
    val expireAt: String
)