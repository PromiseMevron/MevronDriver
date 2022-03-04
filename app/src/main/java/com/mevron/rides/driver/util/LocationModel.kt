package com.mevron.rides.driver.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(
    val lat: Double,
    val lng: Double,
    val address: String
): Parcelable

