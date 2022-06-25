package com.mevron.rides.driver.sidemenu.settingsandprofile.data.model

import com.google.gson.annotations.SerializedName

data class GetProfileSuccess(
    @SerializedName("data")
    val getProfileData: GetProfileData,
    val message: String,
    val status: String
)