package com.mevron.rides.driver.cashout.domain.usecase

import com.mevron.rides.driver.cashout.domain.repository.IPayOutRepository
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(private val repository: IPayOutRepository) {
    suspend operator fun invoke() = repository.getCards()
}