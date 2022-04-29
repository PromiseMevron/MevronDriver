package com.mevron.rides.driver.updateprofile.domain.usecase.carproperties

import com.mevron.rides.driver.updateprofile.domain.repository.ICarPropertiesRepository
import javax.inject.Inject

class GetCarYearsUseCase @Inject constructor(private val repository: ICarPropertiesRepository) {

    suspend operator fun invoke(params: HashMap<String, String>) = repository.getCarYears(params)
}