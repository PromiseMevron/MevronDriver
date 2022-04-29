package com.mevron.rides.driver.updateprofile.domain.usecase.carproperties

import com.mevron.rides.driver.updateprofile.domain.repository.ICarPropertiesRepository
import javax.inject.Inject

class GetCarMakesUseCase @Inject constructor(private val repository: ICarPropertiesRepository) {

    suspend operator fun invoke() = repository.getCarMakes()
}