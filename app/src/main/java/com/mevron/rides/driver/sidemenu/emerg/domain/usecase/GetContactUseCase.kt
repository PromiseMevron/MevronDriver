package com.mevron.rides.driver.sidemenu.emerg.domain.usecase

import com.mevron.rides.driver.sidemenu.emerg.domain.repository.IEmergencyRepository
import javax.inject.Inject

class GetContactUseCase @Inject constructor(private val repository: IEmergencyRepository) {
    suspend operator fun invoke() = repository.getEmergencyContact()
}