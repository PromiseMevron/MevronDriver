package com.mevron.rides.driver.documentcheck.data.model

import com.google.gson.annotations.SerializedName

data class DocData(
    @SerializedName("car_properties")
    val carProperties: CarProperties,
    val documents: List<Document>
)