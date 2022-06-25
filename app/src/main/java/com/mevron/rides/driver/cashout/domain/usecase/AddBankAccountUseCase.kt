package com.mevron.rides.driver.cashout.domain.usecase

import com.mevron.rides.driver.cashout.data.model.AddBankAccountSpecification
import com.mevron.rides.driver.cashout.domain.repository.IPayOutRepository
import javax.inject.Inject

class AddBankAccountUseCase @Inject constructor(private val repository: IPayOutRepository) {
    suspend operator fun invoke(data: AddBankAccountSpecification) = repository.addBank(data = data)
}