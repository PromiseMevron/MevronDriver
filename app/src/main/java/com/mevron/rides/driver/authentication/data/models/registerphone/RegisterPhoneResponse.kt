package com.mevron.rides.driver.authentication.data.models.registerphone

import com.google.gson.annotations.SerializedName

data class RegisterPhoneResponse(
    @SerializedName("success") val successData: SuccessData
)