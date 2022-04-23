package com.mevron.rides.driver.authentication.data.models.createaccount

import com.google.gson.annotations.SerializedName

data class CreateAccountResponse(
    @SerializedName("success")
    val createSuccess: CreateSuccess

)

data class CreateSuccess(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)