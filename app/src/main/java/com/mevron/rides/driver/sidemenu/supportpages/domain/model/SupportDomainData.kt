package com.mevron.rides.driver.sidemenu.supportpages.domain.model

data class SupportDomainData(
    val notifications: List<Supports>
)

data class Supports(
    val heading: String,
    val subHeading: String
)
