package com.mevron.rides.driver.cashout.domain.model

import com.google.gson.annotations.SerializedName

data class PaymentDetailsDomainData(
    val balance: String,
    @SerializedName("data")
    val data: List<PaymentDetailsDomainDatum>,
    val nextPaymentDate: String
)


data class PaymentBalanceDetailsDomainData(
    val balance: String,
    @SerializedName("data")
    val data: List<PaymentBalanceDetailsDomainDatum>,
    val nextPaymentDate: String
)

data class PaymentBalanceDetailsDomainDatum(
    val date: String,
    val data: List<PaymentDetailsDomainDatum>
)


data class PaymentDetailsDomainDatum(
    val amount: String,
    val date: String,
    val icon: String,
    val method: String,
    val narration: String,
    val time: String
)
