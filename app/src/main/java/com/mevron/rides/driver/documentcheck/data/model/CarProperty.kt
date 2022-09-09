package com.mevron.rides.driver.documentcheck.data.model

import com.mevron.rides.driver.home.model.documents.Document

data class CarProperty(
    val color: String,
    val document: List<Document>,
    val id: String,
    val make: String,
    val model: String,
    val plateNumber: String
)