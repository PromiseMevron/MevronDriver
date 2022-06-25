package com.mevron.rides.driver.sidemenu.supportpages.domain.usecase

import com.mevron.rides.driver.sidemenu.supportpages.domain.repository.ISupportRepository
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(private val repository: ISupportRepository) {
    suspend operator fun invoke() = repository.getNotifications()
}