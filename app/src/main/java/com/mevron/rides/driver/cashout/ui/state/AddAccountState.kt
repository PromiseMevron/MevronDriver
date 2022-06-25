package com.mevron.rides.driver.cashout.ui.state

import com.mevron.rides.driver.cashout.data.model.BankInfo
import com.mevron.rides.driver.cashout.domain.model.AddAccount

data class AddAccountState(
    val loading: Boolean,
    val getSuccess: Boolean,
    val errorMessage: String,
    val postSuccess: Boolean,
    val updateRecord: Boolean,
    val isDefault: Int,
    val data: List<BankInfo>,
    val setUpData: List<AddAccount>
) {
    companion object {
        val EMPTY = AddAccountState(
            loading = false,
            getSuccess = false,
            postSuccess = false,
            errorMessage = "",
            updateRecord = false,
            isDefault = 0,
            data = arrayListOf(),
            setUpData = arrayListOf()
        )
    }
}
