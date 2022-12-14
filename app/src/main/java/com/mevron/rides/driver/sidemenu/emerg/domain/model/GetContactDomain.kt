package com.mevron.rides.driver.sidemenu.emerg.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class  GetContactDomain(val savedAddresses: List<GetContactDomainData>?)

@Parcelize
data class GetContactDomainData(
    val id: String,
    val name: String,
    val phone: String,
    val details: List<Int>
): Parcelable
