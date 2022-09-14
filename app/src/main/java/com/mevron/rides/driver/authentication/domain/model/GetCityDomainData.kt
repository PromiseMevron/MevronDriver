package com.mevron.rides.driver.authentication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class GetCityDomainData(val cities: List<GetCitiesData>)

@Parcelize
data class GetCitiesData(
    val name: String,
    val id: String,
): Parcelable