package com.mevron.rides.driver.cashout.domain.repository

import com.mevron.rides.driver.cashout.data.model.AddAccountPayload
import com.mevron.rides.driver.cashout.data.model.AddBankAccountSpecification
import com.mevron.rides.driver.cashout.data.model.CashActionData
import com.mevron.rides.driver.cashout.data.model.GetLinkAmount
import com.mevron.rides.driver.domain.DomainModel

interface IPayOutRepository {
   suspend fun getWalletDetails(): DomainModel
    suspend fun addBank(data : AddBankAccountSpecification): DomainModel
    suspend fun cashOut(data: CashActionData): DomainModel
    suspend fun addFund(data: CashActionData): DomainModel
    suspend fun bankSpecification(): DomainModel
    suspend fun getCards(): DomainModel
    suspend fun getPaymentLink(data: GetLinkAmount): DomainModel
   suspend fun confirmPayment(uiid: String): DomainModel
    suspend fun addNigBanks(data: AddAccountPayload): DomainModel
    suspend fun resolveASccountNumber(data: AddAccountPayload): DomainModel
    suspend fun getBankList(): DomainModel
}