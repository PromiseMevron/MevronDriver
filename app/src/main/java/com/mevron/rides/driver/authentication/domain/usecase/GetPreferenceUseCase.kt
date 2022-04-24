package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import javax.inject.Inject

class GetPreferenceUseCase @Inject constructor(private val repository: IPreferenceRepository) {

    operator fun invoke(key: String) = repository.getStringForKey(key = key)
}