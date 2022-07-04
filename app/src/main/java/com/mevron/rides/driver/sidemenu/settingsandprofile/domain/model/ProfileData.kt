package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model

import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.Review

data class ProfileData(
    val email: String = "",
    val emailStatus: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val phoneNumberStatus: Int = 0,
    val profilePicture: String = "",
    val rating: String = "",
    val type: String = "",
    val uuid: String = "",
    val about: String = "",
    val acceptanceRate: String = "",
    val cancellationRate: String = "",
    val country: String = "",
    val currency: String = "",
    val reviews: List<Review> = mutableListOf(),
    val tripsCompleted: String = "",
)
