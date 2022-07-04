package com.mevron.rides.driver.sidemenu.settingsandprofile.data.model

import com.google.gson.annotations.SerializedName

data class GetProfileResponse(
    @SerializedName("success")
    val getProfileSuccess: GetProfileSuccess
)