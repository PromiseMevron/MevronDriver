package com.mevron.rides.driver.sidemenu.savedplaces.domain.usecases

import com.mevron.rides.driver.sidemenu.savedplaces.data.model.UpdateAddress
import com.mevron.rides.driver.sidemenu.savedplaces.domain.repository.IAddressRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(private val repository: IAddressRepository) {
    suspend operator fun invoke(identifier: String, data: UpdateAddress) =
        repository.updateAnAddress(identifier = identifier, data = data)
}