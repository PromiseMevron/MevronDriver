package com.mevron.rides.driver.sidemenu.settingsandprofile.data.model

data class Review(
    val avatar: String = "",
    val comment: List<String> = mutableListOf(),
    val message: String = "",
    val moment: String = "",
    val rating: String = "",
    val title: String = ""
)