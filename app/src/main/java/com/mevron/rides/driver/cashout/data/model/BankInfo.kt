package com.mevron.rides.driver.cashout.data.model

data class BankInfo(
    val name: String,
    val value: String
)

data class BankRawInfo(
    val name: String,
    val value: String,
    val index: Int
)