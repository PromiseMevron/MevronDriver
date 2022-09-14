package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.data.models.createaccount.GetCityRequest
import com.mevron.rides.driver.authentication.domain.repository.IAuthRepository
import javax.inject.Inject

class GetCityUseCase @Inject constructor(private val repository: IAuthRepository) {

    suspend operator fun invoke(getCitiesData: GetCityRequest) =
        repository.getCities(getCitiesData)
}