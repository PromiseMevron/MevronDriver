package com.mevron.rides.driver.updateprofile.data.model

import com.google.gson.annotations.SerializedName

class CarModelResponse(
    @SerializedName("success")
    val carModelSuccessData: CarModelSuccessData
)

data class CarModelSuccessData(
    @SerializedName("data")
    val carModelList: List<CarModelData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class CarModelData(
    @SerializedName("Model")
    val model: String,
    @SerializedName("id")
    val id: Int
)