package com.mevron.rides.driver.cashout.data.model.banklist

data class BnakListResponse(
    val `data`: List<Data>,
    val message: String,
    val status: String
)