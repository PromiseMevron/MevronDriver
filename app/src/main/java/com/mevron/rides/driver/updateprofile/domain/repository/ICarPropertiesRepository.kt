package com.mevron.rides.driver.updateprofile.domain.repository

import com.mevron.rides.driver.domain.DomainModel

interface ICarPropertiesRepository {
    suspend fun getCarMakes(): DomainModel
    suspend fun getCarModels(params: HashMap<String, String>): DomainModel
    suspend fun getCarYears(params: HashMap<String, String>): DomainModel
}