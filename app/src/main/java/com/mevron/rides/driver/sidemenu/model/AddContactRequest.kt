package com.mevron.rides.driver.sidemenu.model

data class AddContactRequest(
    val sets: List<Set>
)

data class Set(
    val name: String,
    val phoneNumber: String
)