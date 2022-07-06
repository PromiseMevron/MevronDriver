package com.mevron.rides.driver.documentcheck.data.model

import com.google.gson.annotations.SerializedName

data class GetDocumentResponse(
    @SerializedName("success")
    val docSuccess: DocSuccess
)