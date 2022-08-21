package com.mevron.rides.driver.cashout.domain.usecase

import com.mevron.rides.driver.cashout.data.model.GetLinkAmount
import com.mevron.rides.driver.cashout.domain.repository.IPayOutRepository
import javax.inject.Inject

class GetPaymentLinkUseCase @Inject constructor(private val repository: IPayOutRepository) {
    suspend operator fun invoke(data: GetLinkAmount) = repository.getPaymentLink(data)
}