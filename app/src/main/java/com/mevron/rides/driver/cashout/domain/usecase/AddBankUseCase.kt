package com.mevron.rides.driver.cashout.domain.usecase

import com.mevron.rides.driver.cashout.data.model.AddAccountPayload
import com.mevron.rides.driver.cashout.domain.repository.IPayOutRepository
import javax.inject.Inject


class AddBankUseCase @Inject constructor(private val repository: IPayOutRepository) {
    suspend operator fun invoke(data: AddAccountPayload) = repository.addNigBanks(data = data)
}