package com.mevron.rides.driver.home.domain.usecase

import com.mevron.rides.driver.home.domain.ISocketDataRepository
import javax.inject.Inject

class GetSocketStatusUseCase @Inject constructor(
    val repository: ISocketDataRepository
) {
    operator fun invoke() = repository.getStateMachineState
}