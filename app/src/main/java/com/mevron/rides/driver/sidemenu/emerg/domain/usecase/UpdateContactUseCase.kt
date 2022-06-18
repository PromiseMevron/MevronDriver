package com.mevron.rides.driver.sidemenu.emerg.domain.usecase

import com.mevron.rides.driver.sidemenu.emerg.data.model.UpdateEmergencyContact
import com.mevron.rides.driver.sidemenu.emerg.domain.repository.IEmergencyRepository
import javax.inject.Inject

class UpdateContactUseCase @Inject constructor(private val repository: IEmergencyRepository) {
    suspend operator fun invoke(id: String, data: UpdateEmergencyContact) =
        repository.updateEmergencyContact(id, data)
}