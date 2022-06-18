package com.mevron.rides.driver.cashout.domain.usecase

import com.mevron.rides.driver.cashout.data.model.CashOutData
import com.mevron.rides.driver.cashout.domain.repository.IPayOutRepository
import javax.inject.Inject

class CashOutUseCase @Inject constructor(private val repository: IPayOutRepository) {
    suspend operator fun invoke(data: CashOutData) = repository.cashOut(data = data)
}