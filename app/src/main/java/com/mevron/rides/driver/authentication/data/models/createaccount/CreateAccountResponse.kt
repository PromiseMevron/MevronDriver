package com.mevron.rides.driver.authentication.data.models.createaccount

import com.google.gson.annotations.SerializedName

class CreateAccountResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)