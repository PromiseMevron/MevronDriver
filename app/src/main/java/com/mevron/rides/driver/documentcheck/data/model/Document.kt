package com.mevron.rides.driver.documentcheck.data.model

data class Document(
    val id: Int,
    val name: String,
    val status: Int,
    val url: String,
    val type: String? = null,
)