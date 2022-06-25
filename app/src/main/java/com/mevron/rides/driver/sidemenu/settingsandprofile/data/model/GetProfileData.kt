package com.mevron.rides.driver.sidemenu.settingsandprofile.data.model

data class GetProfileData(
    val email: String? = "",
    val emailStatus: Int = 0,
    val firstName: String? = "",
    val lastName: String? = "",
    val phoneNumber: String = "",
    val phoneNumberStatus: Int = 0,
    val profilePicture: String? = "",
    val rating: String? = "",
    val type: String? = "",
    val uuid: String? = ""
)