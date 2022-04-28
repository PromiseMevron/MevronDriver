package com.mevron.rides.driver.updateprofile.data.model

import com.google.gson.annotations.SerializedName

class CarMakesResponse(
    @SerializedName("success")
    val carMakesSuccessData: CarMakesSuccessData
)

data class CarMakesSuccessData(
    @SerializedName("data")
    val carMakesList: List<CarMakesData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class CarMakesData(
    @SerializedName("Make")
    val make: String,
    @SerializedName("id")
    val id: Int
)