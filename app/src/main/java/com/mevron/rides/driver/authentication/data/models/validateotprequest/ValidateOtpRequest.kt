package com.mevron.rides.driver.authentication.data.models.validateotprequest

import com.google.gson.annotations.SerializedName

class ValidateOtpRequest(
    @SerializedName("code")
    val code: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)