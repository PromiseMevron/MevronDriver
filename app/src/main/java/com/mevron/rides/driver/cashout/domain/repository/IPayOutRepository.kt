package com.mevron.rides.driver.cashout.domain.repository

import com.mevron.rides.driver.cashout.data.model.AddBankAccountSpecification
import com.mevron.rides.driver.cashout.data.model.CashOutData
import com.mevron.rides.driver.domain.DomainModel

interface IPayOutRepository {
   suspend fun getWalletDetails(): DomainModel
    suspend fun addBank(data : AddBankAccountSpecification): DomainModel
    suspend fun cashOut(data: CashOutData): DomainModel
    suspend fun bankSpecification(): DomainModel
}