package com.mevron.rides.driver.cashout.data.repository

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.cashout.data.model.AddBankAccountSpecification
import com.mevron.rides.driver.cashout.data.model.CashOutData
import com.mevron.rides.driver.cashout.data.model.GetBankSpecifications
import com.mevron.rides.driver.cashout.data.model.PaymentDetailsResponse
import com.mevron.rides.driver.cashout.data.network.PaymentAPI
import com.mevron.rides.driver.cashout.domain.model.*
import com.mevron.rides.driver.cashout.domain.repository.IPayOutRepository
import com.mevron.rides.driver.domain.DomainModel

class PayOutRepository(private val api: PaymentAPI) : IPayOutRepository {

    override suspend fun getWalletDetails(): DomainModel = api.getWalletDetails().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Wallet details not found"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    override suspend fun cashOut(data: CashOutData): DomainModel = api.cashOut(data = data).let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Error in cash out"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    override suspend fun addBank(data: AddBankAccountSpecification): DomainModel =
        api.addBank(data = data).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel()
                    ?: DomainModel.Error(Throwable("Error in adding bank account"))
            } else {
                DomainModel.Error(Throwable(it.errorBody().toString()))
            }
        }

    override suspend fun bankSpecification(): DomainModel = api.bankSpecification().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel()
                ?: DomainModel.Error(Throwable("Error in getting required specifications"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    private fun PaymentDetailsResponse.toDomainModel() = DomainModel.Success(
        data = PaymentDetailsDomainData(
            balance = this.paySuccess.payData.balance,
            nextPaymentDate = this.paySuccess.payData.nextPaymentDate,
            data = this.paySuccess.payData.payData.map {
                PaymentDetailsDomainDatum(
                    amount = it.amount, date = it.date,
                    icon = it.icon,
                    method = it.method,
                    narration = it.narration,
                    time = it.time
                )
            }
        )
    )

    private fun GeneralResponse.toDomainModel() = DomainModel.Success(
        data = PaymentDomainData(
            status = this.success.status,
            message = this.success.message
        )
    )

    private fun GetBankSpecifications.toDomainModel() = DomainModel.Success(
        data = AddAccountDomainData(
            param = this.specificSuccess.specdata.infoData.map {
                AddAccount(title = it.title, name = it.name)
            }
        )
    )

}