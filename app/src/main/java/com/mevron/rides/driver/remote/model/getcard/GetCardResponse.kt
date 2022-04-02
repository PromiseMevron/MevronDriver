package com.mevron.rides.driver.remote.model.getcard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class GetCardResponse(
    val success: Success
)

data class Success(
    val `data`: List<Data>,
    val message: String,
    val status: String
)

@Parcelize
data class Data(
    val bin: String,
    val brand: String,
    val expiryMonth: String,
    val expiryYear: String,
    val lastDigits: String,
    val type: String,
    val uuid: String
): Parcelable