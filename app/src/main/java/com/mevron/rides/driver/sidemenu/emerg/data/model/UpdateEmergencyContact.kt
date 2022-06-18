package com.mevron.rides.driver.sidemenu.emerg.data.model

data class UpdateEmergencyContact(
    val details: List<Int>,
    val name: String,
    val phoneNumber: String
)