package com.mevron.rides.driver.home.model.documents

data class Document(
    val id: Int,
    val name: String,
    val status: Int,
    val url: String?
)