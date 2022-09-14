package com.mevron.rides.driver.sidemenu.vehicle.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AllVehicleDomainData(val data: List<AllVehicleDomainDatum>)

@Parcelize
data class AllVehicleDomainDatum(
    val color: String,
    val id: Int,
    val image: String,
    val make: String,
    val model: String,
    val plateNumber: String,
    val uuid: String,
    val year: String,
    val preference: Boolean
): Parcelable
