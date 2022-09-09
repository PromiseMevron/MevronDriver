package com.mevron.rides.driver.documentcheck.ui

import com.mevron.rides.driver.documentcheck.data.model.CarProperty
import com.mevron.rides.driver.home.model.documents.CarProperties
import com.mevron.rides.driver.home.model.documents.Document

data class DocumentState(
    val loading: Boolean,
    val documentStatus: MutableList<Document>,
    val error: String,
    val carProperties: MutableList<CarProperty>
) {
    companion object {
        val EMPTY = DocumentState(
            loading = false,
            error = "",
            documentStatus = arrayListOf(),
            carProperties = arrayListOf()
        )
    }
}
