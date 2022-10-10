package com.mevron.rides.driver.updateprofile.domain.model

data class CarMakesDomainData(val makes: List<CarMake>)

class CarMake(
    val make: String
)