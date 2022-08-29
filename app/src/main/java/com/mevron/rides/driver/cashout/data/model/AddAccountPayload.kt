package com.mevron.rides.driver.cashout.data.model

data class AddAccountPayload(
    val account_name: String? = null,
    val account_number: String,
    val bank_code: String? = null,
    val bank_name: String? = null,
    val account_bank: String? = null
)