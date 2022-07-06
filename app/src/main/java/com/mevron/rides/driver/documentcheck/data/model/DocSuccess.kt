package com.mevron.rides.driver.documentcheck.data.model

import com.google.gson.annotations.SerializedName

data class DocSuccess(
    @SerializedName("data")
    val docData: DocData,
    val message: String,
    val status: String
)