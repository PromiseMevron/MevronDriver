package com.mevron.rides.driver.updateprofile.domain.usecase.carproperties

import com.mevron.rides.driver.updateprofile.domain.repository.ICarPropertiesRepository
import javax.inject.Inject

class GetCarModelsUseCase @Inject constructor(private val repository: ICarPropertiesRepository) {

    suspend operator fun invoke(params: HashMap<String, String>) = repository.getCarModels(params)
}