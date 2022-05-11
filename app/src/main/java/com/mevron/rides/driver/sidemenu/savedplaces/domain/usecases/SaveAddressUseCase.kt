package com.mevron.rides.driver.sidemenu.savedplaces.domain.usecases

import com.mevron.rides.driver.sidemenu.savedplaces.data.model.SaveAddressRequest
import com.mevron.rides.driver.sidemenu.savedplaces.domain.repository.IAddressRepository
import javax.inject.Inject

class SaveAddressUseCase @Inject constructor(private val repository: IAddressRepository) {
    suspend operator fun invoke(data: SaveAddressRequest) = repository.addAnAddress(data)
}