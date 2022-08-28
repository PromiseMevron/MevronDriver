package com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.payment

data class Bank (
    val account_number: String,
    val bank_name: String,
    val bank_code: String,
    val default: Int,
    val uuid: String,
    val account_name: String
        )