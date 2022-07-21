package com.mevron.rides.driver.cashout.domain.usecase

import com.mevron.rides.driver.cashout.data.model.CashActionData
import com.mevron.rides.driver.cashout.domain.repository.IPayOutRepository
import javax.inject.Inject

class AddFundUseCase  @Inject constructor(private val repository: IPayOutRepository) {
    suspend operator fun invoke(data: CashActionData) = repository.addFund(data = data)
}