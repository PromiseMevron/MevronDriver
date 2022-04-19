package com.mevron.rides.driver.authentication.data.models.registerphone

import com.google.gson.annotations.SerializedName

data class SuccessData(
    @SerializedName("data")
    val phoneCodeData: PhoneCodeData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)