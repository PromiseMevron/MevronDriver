package com.mevron.rides.driver.updateprofile.domain.model

data class CarYearDomainData(
    val carYearDataList: List<CarYearData>
)

data class CarYearData(
    val year: String,
    val id: Int
)