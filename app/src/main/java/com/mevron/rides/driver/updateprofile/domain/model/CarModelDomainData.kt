package com.mevron.rides.driver.updateprofile.domain.model

data class CarModelDomainData(val carModels: List<CarModel>)

data class CarModel(
    val model: String,
    val id: Int
)