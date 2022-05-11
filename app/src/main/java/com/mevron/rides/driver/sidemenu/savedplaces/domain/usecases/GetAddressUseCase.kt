package com.mevron.rides.driver.sidemenu.savedplaces.domain.usecases

import com.mevron.rides.driver.sidemenu.savedplaces.domain.repository.IAddressRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(private val repository: IAddressRepository) {
    suspend operator fun invoke() = repository.getSavedAddresses()
}