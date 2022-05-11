package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.IHomeScreenRepository
import javax.inject.Inject

class ToggleOnlineStatusUseCase @Inject constructor(private val repository: IHomeScreenRepository) {
    suspend operator fun invoke() = repository.toggleStatus()
}