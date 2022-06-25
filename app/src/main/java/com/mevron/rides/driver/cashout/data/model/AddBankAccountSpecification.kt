package com.mevron.rides.driver.cashout.data.model

data class AddBankAccountSpecification(
    val bankInfo: List<BankInfo>,
    val status: Int
)