package com.mevron.rides.driver.documentcheck.ui

import com.mevron.rides.driver.documentcheck.data.model.CarProperties
import com.mevron.rides.driver.documentcheck.data.model.Document

data class DocumentState(
    val loading: Boolean,
    val documentStatus: MutableList<Document>,
    val error: String,
    val carProperties: CarProperties
) {
    companion object {
        val EMPTY = DocumentState(
            loading = false,
            error = "",
            documentStatus = arrayListOf(),
            carProperties = CarProperties("", "", "", "")
        )
    }
}
