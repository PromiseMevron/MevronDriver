package com.mevron.rides.driver.updateprofile.domain.usecase.updateprofile

import com.mevron.rides.driver.updateprofile.domain.usecase.carproperties.GetCarMakesUseCase
import com.mevron.rides.driver.updateprofile.domain.usecase.carproperties.GetCarModelsUseCase
import com.mevron.rides.driver.updateprofile.domain.usecase.carproperties.GetCarYearsUseCase
import javax.inject.Inject

class AddVehicleUseCaseAggregate @Inject constructor(
    val getCarMakesUseCase: GetCarMakesUseCase,
    val getCarModelsUseCase: GetCarModelsUseCase,
    val getCarYearsUseCase: GetCarYearsUseCase,
    val addVehicleUseCase: AddVehicleUseCase
)