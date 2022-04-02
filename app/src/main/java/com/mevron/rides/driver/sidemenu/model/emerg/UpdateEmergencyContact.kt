package com.mevron.rides.driver.sidemenu.model.emerg

data class UpdateEmergencyContact(
    val details: List<Int>,
    val name: String,
    val phoneNumber: String
)