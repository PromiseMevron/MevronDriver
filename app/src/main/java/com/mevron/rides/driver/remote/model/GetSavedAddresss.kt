package com.mevron.rides.driver.remote.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class GetSavedAddresss(
    val success: Success
)
data class Success(
    val `data`: List<Data>,
    val message: String,
    val status: String
)

@Parcelize
data class Data(
    val address: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val type: String,
    val uuid: String
): Parcelable