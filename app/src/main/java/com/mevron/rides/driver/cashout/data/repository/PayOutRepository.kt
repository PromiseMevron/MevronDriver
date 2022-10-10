package com.mevron.rides.driver.cashout.data.repository

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.cashout.data.model.*
import com.mevron.rides.driver.cashout.data.network.PaymentAPI
import com.mevron.rides.driver.cashout.domain.model.*
import com.mevron.rides.driver.cashout.domain.repository.IPayOutRepository
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.remote.model.getcard.CardData
import com.mevron.rides.driver.remote.model.getcard.GetCardResponse
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.payment.PaymentMethodResponse
import com.mevron.rides.driver.util.Constants

class PayOutRepository(private val api: PaymentAPI) : IPayOutRepository {

    override suspend fun getWalletDetails(): DomainModel = api.getWalletDetails().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Wallet details not found"))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun cashOut(data: CashActionData): DomainModel = api.cashOut(data = data).let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Error in cash out"))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun addFund(data: CashActionData): DomainModel = api.addFund(data = data).let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Error in adding fund"))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun addBank(data: AddBankAccountSpecification): DomainModel =
        api.addBank(data = data).let {
            if (it.isSuccessful) {
                it.body()?.toDomainModel()
                    ?: DomainModel.Error(Throwable("Error in adding bank account"))
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(it)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        }

    override suspend fun bankSpecification(): DomainModel = api.bankSpecification().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel()
                ?: DomainModel.Error(Throwable("Error in getting required specifications"))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun getCards(): DomainModel = api.getCards().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Error in fetching cards"))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun getPaymentLink(data: GetLinkAmount): DomainModel {
        return try {
            val response = api.getPaymentLink(data = data)
            if (response.isSuccessful) {
                DomainModel.Success(
                    data = PaymentLinkDomain(response.body()?.link ?: "")
                )
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(response)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        } catch (error: Throwable) {
            DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun confirmPayment(uiid: String): DomainModel {
        return try {
            val response = api.confirmPayment(uiid.replace("%3F", "?"))
            if (response.isSuccessful) {
                DomainModel.Success(data = Unit)
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(response)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        } catch (error: Throwable) {
            DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun addNigBanks(data: AddAccountPayload): DomainModel {
        return try {
            val response = api.addNigBanks(data)
            if (response.isSuccessful) {
                DomainModel.Success(data = Unit)
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(response)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        } catch (error: Throwable) {
            DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun resolveASccountNumber(data: AddAccountPayload): DomainModel {
        return try {
            val response = api.resolveASccountNumber(data)
            if (response.isSuccessful) {
                DomainModel.Success(data = response.body()?.data?.let {
                    ResolveBankDomainData(account_name = it.account_name, account_number = it.account_number)
                } ?: ResolveBankDomainData("", ""))
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(response)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        } catch (error: Throwable) {
            DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        }
    }

    override suspend fun getBankList(): DomainModel {
        return try {
            val response = api.getBankList()
            if (response.isSuccessful) {
                DomainModel.Success(data = response.body()?.data?.let {
                    BankListDomainData(bankData = it.map {it2 ->
                        BankList(code = it2.code, name = it2.name)
                    })

                } ?: ResolveBankDomainData("Not Found", ""))
            } else {
                val error = HTTPErrorHandler.handleErrorWithCode(response)
                DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
            }
        } catch (error: Throwable) {
            DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        }
    }

    private fun PaymentDetailsResponse.toDomainModel() = DomainModel.Success(
        data = PaymentDetailsDomainData(
            balance = "${this.paySuccess.payData.currencySymbol}${this.paySuccess.payData.balance}",
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

    private fun PaymentMethodResponse.toDomainModel() = DomainModel.Success(
        data = GetCardData(
            cardData = this.success.data.card.map {
                CardData(bin = it.bin, brand = it.brand, expiryYear = it.expiryYear, expiryMonth = it.expiryMonth, lastDigits = it.lastDigits, type = it.type, uuid = it.uuid)
            }, bankData = this.success.data.bank.map {
                GetBankDatum(account_number = it.account_number, bank_code = it.bank_code, bank_name = it.bank_name, default = it.default == 1, uuid = it.uuid, account_name = it.account_name)
            }
        )
    )

}