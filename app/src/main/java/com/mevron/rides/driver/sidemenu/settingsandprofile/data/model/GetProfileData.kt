package com.mevron.rides.driver.sidemenu.settingsandprofile.data.model

data class GetProfileData(
    val about: String = "",
    val acceptanceRate: String = "",
    val cancellationRate: String = "",
    val country: String = "",
    val currency: String = "",
    val email: String = "",
    val emailStatus: Int = 1,
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val phoneNumberStatus: Int = 0,
    val profilePicture: String = "",
    val rating: String = "",
    val reviews: List<Review> = mutableListOf(),
    val tripsCompleted: String = "",
    val uuid: String = "",
    val supportNumber: String? = "",
    val type: String? = "",
)