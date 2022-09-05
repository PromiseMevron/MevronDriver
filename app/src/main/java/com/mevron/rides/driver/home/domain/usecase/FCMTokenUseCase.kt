package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.data.model.home.DeviceID
import com.mevron.rides.driver.home.domain.IHomeScreenRepository
import javax.inject.Inject

class FCMTokenUseCase  @Inject constructor(private val repository: IHomeScreenRepository) {
    //suspend operator fun invoke(id: DeviceID) = repository.sendToken(id)
}