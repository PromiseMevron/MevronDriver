package com.mevron.rides.driver.sidemenu.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class GetContactsResponse(
    val success: Success
)

data class Success(
    val `data`: List<Data>,
    val message: String,
    val status: String
)


@Parcelize
data class Data(
    val id: String,
    val name: String,
    val phoneNumber: String
):Parcelable