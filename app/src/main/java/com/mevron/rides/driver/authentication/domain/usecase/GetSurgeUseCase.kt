package com.mevron.rides.driver.authentication.domain.usecase

import com.mevron.rides.driver.home.domain.ISurgeRepository
import javax.inject.Inject

class GetSurgeUseCase @Inject constructor(private val surgeRepo: ISurgeRepository) {
    operator fun invoke() = surgeRepo.surgeUrl
}