package com.mevron.rides.driver.cashout.domain.model

data class AddAccountDomainData(val param: List<AddAccount>)

data class AddAccount(
    val title: String,
    val name: String
)
