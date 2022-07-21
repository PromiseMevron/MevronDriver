package com.mevron.rides.driver.remote.geoservice.domain.repository

import com.mevron.rides.driver.domain.DomainModel

interface IGoogleRepository {
    suspend fun makeGoogleCall(url: String): DomainModel
}