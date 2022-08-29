package com.mevron.rides.driver.cashout.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class BankListDomainData (
    val bankData: List<BankList>
        )


@Parcelize
data class BankList(
    val code: String,
    val name: String,
): Parcelable