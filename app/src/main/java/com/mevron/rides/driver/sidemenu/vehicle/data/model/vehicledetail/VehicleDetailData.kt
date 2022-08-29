package com.mevron.rides.driver.sidemenu.vehicle.data.model.vehicledetail

import com.mevron.rides.driver.documentcheck.data.model.Document

data class VehicleDetailData(
    val color: String,
    val document: List<Document>,
    val id: Int,
    val image: String,
    val make: String,
    val model: String,
    val plateNumber: String,
    val year: String,
)