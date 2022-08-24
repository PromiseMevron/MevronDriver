package com.mevron.rides.driver.domain

import com.google.gson.annotations.SerializedName

data class SurgeResponse(@SerializedName("surge_url") val url: String)