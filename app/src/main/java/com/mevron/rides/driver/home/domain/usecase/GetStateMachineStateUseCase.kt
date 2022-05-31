package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.IStateMachineRepository
import javax.inject.Inject

data class GetStateMachineStateUseCase @Inject constructor(
    private val repository: IStateMachineRepository
) {
    suspend operator fun invoke() = repository.getStateMachineState()
}