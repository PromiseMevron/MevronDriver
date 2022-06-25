package com.mevron.rides.driver.sidemenu.emerg.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetContactsResponse(
    @SerializedName("success")
    val getContactSuccess: GetContacts
)

data class GetContacts(
    @SerializedName("data")
    val getContactData: List<GetContactData>?,
    val message: String,
    val status: String
)


@Parcelize
data class GetContactData(
    val id: String,
    val name: String,
    val phoneNumber: String
):Parcelable