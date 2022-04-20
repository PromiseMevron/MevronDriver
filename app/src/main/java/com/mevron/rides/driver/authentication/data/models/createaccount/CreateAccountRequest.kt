package com.mevron.rides.driver.authentication.data.models.createaccount

import com.google.gson.annotations.SerializedName

data class CreateAccountRequest(
    @SerializedName("city")
    val city: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("referralCode")
    var referralCode: String? = null
)