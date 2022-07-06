package com.mevron.rides.driver.remote.geoservice.domain.usecase

import com.mevron.rides.driver.remote.geoservice.domain.repository.IGoogleRepository
import javax.inject.Inject

class GetGoogleResponseUseCase @Inject constructor(private val repository: IGoogleRepository) {
    suspend operator fun invoke(url: String) =
        repository.makeGoogleCall(url = url)
}