package com.mevron.rides.driver.documentcheck.data.model

import com.mevron.rides.driver.home.model.documents.Document

data class Data(
    val car_properties: List<CarProperty>,
    val documents: List<Document>
)